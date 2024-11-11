package io.effi.rpc.core;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.common.extension.Attributes;
import io.effi.rpc.common.extension.Scheduler;
import io.effi.rpc.common.url.URLSource;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.core.config.RegistryConfig;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.manager.*;
import io.effi.rpc.event.Event;
import io.effi.rpc.event.EventDispatcher;
import org.intellij.lang.annotations.Language;

/**
 * Manage all entry configurations.
 */
public interface Portal extends Attributes, URLSource, Lifecycle {

    AttributeKey<Portal> KEY = AttributeKey.valueOf(KeyConstant.PORTAL);

    /**
     * Returns current portal name.
     *
     * @return
     */
    String name();

    /**
     * Sets current portal name.
     *
     * @param name
     * @return
     */
    Portal name(String name);

    /**
     * Returns current portal event dispatcher.
     *
     * @return
     */
    EventDispatcher eventDispatcher();

    /**
     * Returns current portal scheduler.
     *
     * @return
     */
    Scheduler scheduler();

    /**
     * Returns current portal environment.
     *
     * @return
     */
    Environment environment();

    /**
     * Returns current portal monitor manager.
     *
     * @return
     */
    MonitorManager monitorManager();

    /**
     * Returns current portal caller manager.
     *
     * @return
     */
    CallerManager callerManager();

    /**
     * Returns current portal server exporter manager.
     *
     * @return
     */
    ServerExporterManager serverExporterManager();

    /**
     * Returns current portal registry config manager.
     *
     * @return
     */
    RegistryConfigManager registryConfigManager();

    /**
     * Returns current portal filter manager.
     *
     * @return
     */
    FilterManager filterManager();

    /**
     * Returns current portal router config manager.
     *
     * @return
     */
    RouterConfigManager routerConfigManager();

    /**
     * Registers a router.
     *
     * @param urlRegex
     * @param targetRegex
     * @return
     */
    default Portal router(@Language("RegExp") String urlRegex, @Language("RegExp") String targetRegex) {
        routerConfigManager().register(urlRegex, targetRegex);
        return this;
    }

    default Portal register(Caller<?>... callers) {
        if (CollectionUtil.isNotEmpty(callers)) {
            for (Caller<?> caller : callers) {
                callerManager().register(caller);
            }
        }
        return this;
    }

    default Portal register(ServerExporter... serverExporters) {
        if (CollectionUtil.isNotEmpty(serverExporters)) {
            for (ServerExporter serverExporter : serverExporters) {
                serverExporterManager().register(serverExporter);
            }
        }
        return this;
    }

    default Portal register(RegistryConfig... registryConfigs) {
        if (CollectionUtil.isNotEmpty(registryConfigs)) {
            for (RegistryConfig registryConfig : registryConfigs) {
                registryConfigManager().register(registryConfig);
            }
        }
        return this;
    }

    default Portal register(String name, Filter filter) {
        filterManager().register(name, filter);
        return this;
    }

    default Portal registerShared(RegistryConfig... registryConfigs) {
        registryConfigManager().registerShared(registryConfigs);
        return this;
    }

    default Portal registerShared(Filter... filters) {
        filterManager().registerShared(filters);
        return this;
    }

    /**
     * Publishes event.
     *
     * @param event
     * @return
     */
    default Portal publishEvent(Event<?> event) {
        eventDispatcher().publish(event);
        return this;
    }

}
