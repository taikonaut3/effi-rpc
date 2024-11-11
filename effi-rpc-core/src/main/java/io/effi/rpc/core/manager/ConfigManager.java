package io.effi.rpc.core.manager;

import io.effi.rpc.core.Lifecycle;
import io.effi.rpc.core.Portal;
import io.effi.rpc.core.config.ApplicationConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * All Config Manager.
 */
@Getter
@Accessors(fluent = true)
public class ConfigManager implements Lifecycle {


    private final ServerExporterManager serverExporterManager;

    private final RegistryConfigManager registryConfigManager;

    private final RouterConfigManager routerConfigManager;

    private final FilterManager filterManager;

    @Setter
    private ApplicationConfig applicationConfig;

    public ConfigManager(Portal portal) {
        serverExporterManager = new ServerExporterManager(portal);
        registryConfigManager = new RegistryConfigManager(portal);
        routerConfigManager = new RouterConfigManager(portal);
        filterManager = new FilterManager(portal);
        this.applicationConfig = new ApplicationConfig();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        serverExporterManager.clear();
        routerConfigManager.clear();
        filterManager.clear();
    }

}
