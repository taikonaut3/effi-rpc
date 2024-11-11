package io.effi.rpc.protocol.support;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.config.AbstractURLConfig;
import io.effi.rpc.core.config.RegistryConfig;
import io.effi.rpc.protocol.support.builder.RegistryConfigBuilder;

/**
 * Default implementation of {@link RegistryConfig}.
 */
public class DefaultRegistryConfig extends AbstractURLConfig implements RegistryConfig {

    DefaultRegistryConfig(String name, URL url) {
        super(name, url);
    }

    /**
     * Creates and returns a new {@link DefaultRegistryConfigBuilder} instance for constructing
     * {@link DefaultRegistryConfig} objects using a fluent API.
     *
     * @return a new {@link DefaultRegistryConfigBuilder} instance
     */
    public static DefaultRegistryConfigBuilder builder() {
        return new DefaultRegistryConfigBuilder();
    }

    /**
     * Builder class for {@link DefaultRegistryConfig}.
     */
    public static class DefaultRegistryConfigBuilder extends RegistryConfigBuilder<DefaultRegistryConfig, DefaultRegistryConfigBuilder> {

        @Override
        protected DefaultRegistryConfig newRegistryConfig(URL url) {
            return new DefaultRegistryConfig(name, url);
        }
    }
}

