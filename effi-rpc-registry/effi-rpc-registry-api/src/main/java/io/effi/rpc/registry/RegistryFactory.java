package io.effi.rpc.registry;

import io.effi.rpc.common.extension.resoruce.Cleanable;
import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.common.url.URL;

import static io.effi.rpc.common.constant.Component.Registry.CONSUL;

/**
 * Factory for creating and acquiring instances of {@link RegistryService}.
 * Provides methods to manage registry services for service discovery and
 * registration.
 */
@Extensible(CONSUL)
public interface RegistryFactory extends Cleanable {

    /**
     * Acquires a {@link RegistryService} instance for the specified URL.
     *
     * @param url the URL representing the configuration for the registry service
     * @return the acquired {@link RegistryService} instance
     */
    RegistryService acquire(URL url);
}



