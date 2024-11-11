package io.effi.rpc.governance.discovery;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.registry.RegistryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of {@link ServiceDiscovery}.
 * <p>Deduplication based on address.</p>
 */
@Extension(Component.DEFAULT)
public class DefaultServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceDiscovery.class);

    @Override
    public List<URL> discover(CallInvocation<?> invocation, URL... registryConfigs) {
        if (CollectionUtil.isEmpty(registryConfigs)) {
            logger.warn("registry config is empty");
        }
        List<URL> availableServiceUrls = new ArrayList<>();
        URL url = invocation.callerUrl();
        for (URL registryUrl : registryConfigs) {
            var registryService = ExtensionLoader.loadExtension(RegistryFactory.class, registryUrl.protocol()).acquire(registryUrl);
            List<URL> discoverUrls = registryService.discover(url);
            if (CollectionUtil.isNotEmpty(discoverUrls)) {
                for (URL discoverUrl : discoverUrls) {
                    CollectionUtil.addToList(
                            availableServiceUrls,
                            (existUrl, newUrl) -> Objects.equals(existUrl.address(), newUrl.address()),
                            discoverUrl);
                }
            }
        }
        if (availableServiceUrls.isEmpty()) {
            throw new RpcException(String.format("Not found available service!,Protocol:%s, Path:%s", url.protocol(), url.path()));
        }
        return availableServiceUrls;
    }
}
