package io.effi.rpc.registry.consul;

import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.registry.AbstractRegistryFactory;
import io.effi.rpc.registry.RegistryService;

import static io.effi.rpc.common.constant.Component.Registry.CONSUL;

/**
 * {@link io.effi.rpc.registry.RegistryFactory} implementation based on consul.
 */
@Extension(CONSUL)
public class ConsulRegistryFactory extends AbstractRegistryFactory {

    @Override
    protected RegistryService create(URL url) {
        return new ConsulRegistryService(url);
    }

}

