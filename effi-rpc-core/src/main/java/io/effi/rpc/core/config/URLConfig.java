package io.effi.rpc.core.config;

import io.effi.rpc.common.url.Parameterization;
import io.effi.rpc.common.url.URLSource;
import io.effi.rpc.core.manager.Manager;

/**
 * URLConfig provides a structure for configurations
 * that can be easily converted to a URL and work with URL parameters
 * to enhance the dynamic capability of configurations.
 * <p>
 * By combining URL-based parameters with key-value configurations,
 * this interface makes it easier to dynamically retrieve and manage
 * settings, improving flexibility when handling configuration data.
 * <p>
 * Classes that implement this interface can leverage URL parameterization for more dynamic and customizable behavior.
 */
public interface URLConfig extends URLSource, Manager.Key, Parameterization {
    /**
     * Returns the unique name associated with this configuration.
     * This name must be unique across all configurations and is used to
     * distinctly identify the service, component, or resource.
     *
     * @return the unique name of this configuration.
     */
    String name();

}

