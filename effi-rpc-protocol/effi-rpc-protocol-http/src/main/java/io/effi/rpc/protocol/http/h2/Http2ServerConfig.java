package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.config.AbstractURLConfig;
import io.effi.rpc.core.config.ServerConfig;

/**
 * Configuration class for HTTP/2 server settings.
 */
public class Http2ServerConfig extends AbstractURLConfig implements ServerConfig {
    Http2ServerConfig(String name, URL url) {
        super(name, url);
    }

    /**
     * Creates and returns a new {@link Http2ServerConfigBuilder} instance for constructing
     * {@link Http2ServerConfig} objects using a fluent API.
     *
     * @return
     */
    public static Http2ServerConfigBuilder builder() {
        return new Http2ServerConfigBuilder();
    }
}
