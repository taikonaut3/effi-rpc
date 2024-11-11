package io.effi.rpc.core.manager;

import io.effi.rpc.core.Portal;
import io.effi.rpc.core.config.RegistryConfig;

/**
 * Manage the registration and retrieval of {@link RegistryConfig} instances.
 */
public class RegistryConfigManager extends SharableManager<RegistryConfig> {

    public RegistryConfigManager(Portal portal) {
        super(portal);
    }


}
