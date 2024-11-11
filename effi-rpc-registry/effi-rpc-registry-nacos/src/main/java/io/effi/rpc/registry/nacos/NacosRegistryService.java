package io.effi.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.registry.AbstractRegistryService;
import io.effi.rpc.registry.support.RegisterTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * {@link io.effi.rpc.registry.RegistryService} implementation based on the nacos client.
 * For more information,refer to the <a href = "https://github.com/alibaba/nacos">nacos</a>.
 */
public class NacosRegistryService extends AbstractRegistryService {

    private static final Logger logger = LoggerFactory.getLogger(NacosRegistryService.class);

    private static String NACOS_PROJECT_NAME;

    private NamingService namingService;

    protected NacosRegistryService(URL url) {
        super(url);
    }

    @Override
    public boolean isActive() {
        return namingService.getServerStatus().equals("UP");
    }

    @Override
    public void connect(URL url) {
        try {
            namingService = NamingFactory.createNamingService(url.address());
            isActive();
        } catch (NacosException e) {
            logger.error("Connect to nacos: {} failed", url.address());
            throw RpcException.wrap(e);
        }
    }

    @Override
    public BiConsumer<RegisterTask, Map<String, String>> buildRegisterTask(URL url) {
        String serviceName = serviceName(url);
        if (NACOS_PROJECT_NAME == null) {
            synchronized (serviceName) {
                if (NACOS_PROJECT_NAME == null) {
                    NACOS_PROJECT_NAME = serviceName;
                    // nacos <project.name>
                    System.setProperty("project.name", serviceName);
                }
            }
        }
        return (registerTask, metaData) -> {
            Instance instance = new Instance();
            instance.setInstanceId(instanceId(url));
            instance.setIp(url.host());
            instance.setPort(url.port());
            instance.setMetadata(metaData);
            try {
                namingService.registerInstance(serviceName, instance);
            } catch (NacosException e) {
                logger.error("RegisterInstance is failed from nacos: service:{}-{}", serviceName, url.address());
                throw RpcException.wrap(e);
            }
        };
    }

    @Override
    public void deregister(URL url) {
        String serviceName = serviceName(url);
        try {
            namingService.deregisterInstance(serviceName, url.host(), url.port());
        } catch (NacosException e) {
            logger.error("DeregisterInstance is failed from nacos: service:{}-{}", serviceName, url.address());
            throw RpcException.wrap(e);
        }
    }

    @Override
    protected List<URL> doDiscover(URL url) {
        String serviceName = serviceName(url);
        ArrayList<URL> urls = new ArrayList<>();
        try {
            List<Instance> instances = namingService.selectInstances(serviceName, true);
            for (Instance instance : instances) {
                String protocol = instance.getMetadata().get(KeyConstant.PROTOCOL);
                if (!StringUtil.isBlank(protocol) && protocol.equalsIgnoreCase(url.protocol())) {
                    urls.add(instanceToUrl(instance));
                }
            }
        } catch (NacosException e) {
            logger.error("SelectInstances is failed from nacos for url:{}", url);
            throw RpcException.wrap(e);
        }
        return urls;
    }

    @Override
    protected void subscribeService(URL url) {
        String serviceName = serviceName(url);
        try {
            namingService.subscribe(serviceName, event -> {
                if (event instanceof NamingEvent namingEvent) {
                    List<Instance> instances = namingEvent.getInstances();
                    List<String> healthServerUrls = instances.stream()
                            .filter(instance -> instance.isHealthy() && instance.getMetadata().containsKey(KeyConstant.PROTOCOL))
                            .map(instance -> instanceToUrl(instance).toString())
                            .toList();
                    discoverHealthServices.put(serviceName, healthServerUrls);
                }
            });
        } catch (NacosException e) {
            logger.error("Subscribe is failed from nacos for service:{}", serviceName);
            throw RpcException.wrap(e);
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            namingService.shutDown();
        } catch (NacosException e) {
            throw RpcException.wrap(e);
        }
    }

    private URL instanceToUrl(Instance instance) {
        String protocol = instance.getMetadata().get(KeyConstant.PROTOCOL).toLowerCase();
        return URL.builder()
                .protocol(protocol)
                .address(NetUtil.toAddress(instance.getIp(), instance.getPort()))
                .params(instance.getMetadata())
                .build();
    }
}
