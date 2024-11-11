package io.effi.rpc.protocol.support;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.config.AbstractURLConfig;
import io.effi.rpc.core.config.ServerConfig;
import io.effi.rpc.protocol.support.builder.ServerConfigBuilder;

/**
 * Default implementation of {@link ServerConfig}.
 */
public class DefaultServerConfig extends AbstractURLConfig implements ServerConfig {


    protected DefaultServerConfig(String name, URL url) {
        super(name, url);
    }

    /**
     * Creates and returns a new {@link DefaultServerConfigBuilder} instance for building
     * {@link DefaultServerConfig} objects using a fluent API.
     *
     * @return a new {@link DefaultServerConfigBuilder} instance
     */
    public static DefaultServerConfigBuilder builder() {
        return new DefaultServerConfigBuilder();
    }


    /**
     * Builder class for {@link DefaultServerConfig}.
     */
    public static class DefaultServerConfigBuilder extends ServerConfigBuilder<DefaultServerConfig, DefaultServerConfigBuilder> {

        @Override
        protected DefaultServerConfig newServerConfig(URL url) {
            return new DefaultServerConfig(name, url);
        }
    }
}


