package io.effi.rpc.protocol.support;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Locator;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.config.RegistryConfig;
import io.effi.rpc.governance.discovery.ServiceDiscovery;
import io.effi.rpc.governance.loadbalance.LoadBalancer;
import io.effi.rpc.governance.router.Router;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author WenBo Zhou
 * @Date 2024/10/25 10:45
 */
public class RegistryLocator implements Locator {

    private final String remoteApplication;

    private final List<RegistryConfig> registryConfigs;

    public RegistryLocator(String remoteApplication, List<RegistryConfig> registryConfigs) {
        this.remoteApplication = AssertUtil.notBlank(remoteApplication, "remoteApplication cannot be blank");
        this.registryConfigs = registryConfigs;
    }

    public RegistryLocator(String remoteApplication) {
        this(remoteApplication, null);
    }

    @Override
    public InetSocketAddress locate(CallInvocation<?> invocation) {
        URL url = invocation.callerUrl();
        url.addParam(KeyConstant.SERVICE_NAME, remoteApplication);
        // ServiceDiscovery
        ServiceDiscovery serviceDiscovery = ExtensionLoader.loadExtension(ServiceDiscovery.class, url);
        URL[] registryConfigUrls = registryConfigs(invocation);
        List<URL> availableServiceUrls = serviceDiscovery.discover(invocation, registryConfigUrls);
        // Router
        Portal portal = invocation.portal();
        Router router = portal.get(Router.ATTRIBUTE_KEY);
        if (router == null) {
            String routerName = portal.url().getParam(KeyConstant.ROUTER, Constant.DEFAULT_ROUTER);
            routerName = StringUtil.isBlankOrDefault(routerName, Constant.DEFAULT_ROUTER);
            router = ExtensionLoader.loadExtension(Router.class, routerName);
        }
        List<URL> finalServiceUrls = router.route(invocation, availableServiceUrls);
        // LoadBalance
        LoadBalancer loadBalancer = ExtensionLoader.loadExtension(LoadBalancer.class, url);
        URL chosenUrl = loadBalancer.choose(invocation, finalServiceUrls);
        return NetUtil.toInetSocketAddress(chosenUrl.address());
    }

    private URL[] registryConfigs(CallInvocation<?> invocation) {
        Portal portal = invocation.caller().portal();
        List<RegistryConfig> sharedRegistryConfigs = portal.registryConfigManager().sharedValues();
        List<RegistryConfig> registryConfigs = new ArrayList<>(sharedRegistryConfigs);
        if (CollectionUtil.isNotEmpty(this.registryConfigs)) {
            CollectionUtil.addUnique(registryConfigs, this.registryConfigs.toArray(new RegistryConfig[0]));
        }
        return registryConfigs.stream().map(config -> config.url().replicate()).toArray(URL[]::new);
    }
}
