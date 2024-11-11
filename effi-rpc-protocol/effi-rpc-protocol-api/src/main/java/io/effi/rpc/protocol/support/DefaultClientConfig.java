package io.effi.rpc.protocol.support;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.config.AbstractURLConfig;
import io.effi.rpc.core.config.ClientConfig;
import io.effi.rpc.protocol.support.builder.ClientConfigBuilder;

/**
 * Default implementation of {@link ClientConfig}.
 */
public class DefaultClientConfig extends AbstractURLConfig implements ClientConfig {

    DefaultClientConfig(String name, URL url) {
        super(name, url);
    }

    /**
     * Creates and returns a new {@link DefaultClientConfigBuilder} instance for constructing
     * {@link DefaultClientConfig} objects using a fluent API.
     *
     * @return a new {@link DefaultClientConfigBuilder} instance
     */
    public static DefaultClientConfigBuilder builder() {
        return new DefaultClientConfigBuilder();
    }


    /**
     * Builder class for {@link DefaultClientConfig}.
     */
    public static class DefaultClientConfigBuilder extends ClientConfigBuilder<DefaultClientConfig, DefaultClientConfigBuilder> {

        @Override
        protected DefaultClientConfig newClientConfig(URL url) {
            return new DefaultClientConfig(name, url);
        }
    }
}

