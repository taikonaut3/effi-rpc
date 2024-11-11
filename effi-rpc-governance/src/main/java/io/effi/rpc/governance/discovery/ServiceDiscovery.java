package io.effi.rpc.governance.discovery;

import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;

import java.util.List;

import static io.effi.rpc.common.constant.Component.DEFAULT;

/**
 * Discovers available services from a service registry.
 */
@Extensible(DEFAULT)
public interface ServiceDiscovery {

    /**
     * Retrieves all available services from the registry based on the provided
     * invocation context and registry configurations.
     *
     * @param invocation      the context for the service lookup
     * @param registryConfigs the configurations for accessing the service registry
     * @return a list of URLs representing the available services
     */
    List<URL> discover(CallInvocation<?> invocation, URL... registryConfigs);
}


