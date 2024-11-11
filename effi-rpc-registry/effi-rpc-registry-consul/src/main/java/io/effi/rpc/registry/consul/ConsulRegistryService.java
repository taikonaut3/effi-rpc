package io.effi.rpc.registry.consul;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.LazyInitializer;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.registry.AbstractRegistryService;
import io.effi.rpc.registry.support.RegisterTask;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * {@link io.effi.rpc.registry.RegistryService} implementation based on vertx-consul client.
 * This service is responsible for handling service registration and discovery
 * using Consul as the underlying registry mechanism.
 * For more information,refer to the <a href="https://github.com/vert-x3/vertx-consul-client">vertx-consul-client</a>.
 */
public class ConsulRegistryService extends AbstractRegistryService {

    private static final Logger logger = LoggerFactory.getLogger(ConsulRegistryService.class);

    private static final Vertx VERTX = new LazyInitializer<>(Vertx::vertx).get(false);

    private ConsulClient consulClient;

    protected ConsulRegistryService(URL url) {
        super(url);
    }

    @Override
    public boolean isActive() {
        AtomicBoolean isConnected = new AtomicBoolean(false);
        consulClient.agentInfo()
                .onComplete(ar -> isConnected.set(ar.succeeded()))
                .toCompletionStage().toCompletableFuture().join();
        return isConnected.get();
    }

    @Override
    public void connect(URL url) {
        int connectTimeout = url.getIntParam(KeyConstant.CONNECT_TIMEOUT,
                Constant.DEFAULT_CONNECT_TIMEOUT);
        try {
            ConsulClientOptions options = new ConsulClientOptions()
                    .setHost(url.host())
                    .setPort(url.port())
                    .setTimeout(connectTimeout);
            consulClient = ConsulClient.create(VERTX, options);
            // try to connect consul
            isActive();
        } catch (Exception e) {
            logger.error("Connect to consul: {} failed", url.address());
            throw RpcException.wrap(e);
        }
    }

    @Override
    public BiConsumer<RegisterTask, Map<String, String>> buildRegisterTask(URL url) {
        String serviceName = serviceName(url);
        String serviceId = instanceId(url);
        return (registerTask, metaData) -> {
            ServiceOptions opts = new ServiceOptions()
                    .setName(serviceName)
                    .setId(serviceId)
                    .setAddress(url.host())
                    .setPort(url.port())
                    .setMeta(metaData);
            if (enableHealthCheck) {
                int healthCheckInterval = url.getIntParam(KeyConstant.HEALTH_CHECK_INTERVAL,
                        Constant.DEFAULT_HEALTH_CHECK_INTERVAL);
                CheckOptions checkOpts = new CheckOptions()
                        .setTcp(url.address())
                        .setId(serviceId)
                        .setDeregisterAfter((healthCheckInterval * 10) + "ms")
                        .setInterval(healthCheckInterval + "ms");
                opts.setCheckOptions(checkOpts);
            }
            consulClient.registerService(opts, res -> {
                if (res.succeeded() && registerTask.isFirstRun()) {
                    logger.info("Registered {}: {}", serviceName, serviceId);
                    registerTask.isFirstRun(false);
                } else if (!res.succeeded()) {
                    logger.error("Register {}: {} failed", res.cause(), serviceName, serviceId);
                }
            });
        };
    }

    @Override
    public void deregister(URL url) {
        String serviceId = instanceId(url);
        consulClient.deregisterService(serviceId)
                .onSuccess(v -> logger.info("Deregistered {}: {}", serviceName(url), serviceId))
                .onFailure(cause -> logger.error("Deregister {}: {} failed", cause, serviceName(url), serviceId))
                .toCompletionStage().toCompletableFuture().join();
    }

    @Override
    protected List<URL> doDiscover(URL url) {
        String serviceName = serviceName(url);
        ArrayList<URL> urls = new ArrayList<>();
        // Get the urls of all nodes for health checks
        consulClient.healthServiceNodes(serviceName, true)
                .onFailure(e -> logger.error("Discover {} failed", e, serviceName))
                .onSuccess(result -> {
                    List<ServiceEntry> serviceEntries = result.getList();
                    logger.debug("{} found {} service(s) from <{}>", url.uri(), serviceEntries.size(), serviceName);
                    for (ServiceEntry entry : serviceEntries) {
                        Service service = entry.getService();
                        String protocol = service.getMeta().get(KeyConstant.PROTOCOL);
                        if (!StringUtil.isBlank(protocol) && protocol.equalsIgnoreCase(url.protocol())) {
                            urls.add(serviceEntryToUrl(entry));
                        }
                    }
                }).toCompletionStage().toCompletableFuture().join();
        return urls;
    }

    @Override
    protected void subscribeService(URL url) {
        String serviceName = serviceName(url);
        Watch.service(serviceName, VERTX).setHandler(res -> {
            if (res.succeeded()) {
                List<ServiceEntry> serviceEntries = res.nextResult().getList();
                List<String> healthServerUrls = serviceEntries.stream()
                        .filter(instance -> instance.aggregatedStatus() == CheckStatus.PASSING
                                && instance.getService().getMeta().containsKey(KeyConstant.PROTOCOL))
                        .map(instance -> serviceEntryToUrl(instance).toString())
                        .toList();
                discoverHealthServices.put(serviceName, healthServerUrls);
            }
        }).start();
        logger.info("Subscribe service['{}']", serviceName);
    }

    @Override
    public void close() {
        super.close();
        consulClient.close();
    }

    private URL serviceEntryToUrl(ServiceEntry entry) {
        Service service = entry.getService();
        Map<String, String> meta = service.getMeta();
        String protocol = meta.get(KeyConstant.PROTOCOL).toLowerCase();
        return URL.builder()
                .protocol(protocol)
                .address(NetUtil.toAddress(service.getAddress(), service.getPort()))
                .params(meta)
                .build();
    }
}
