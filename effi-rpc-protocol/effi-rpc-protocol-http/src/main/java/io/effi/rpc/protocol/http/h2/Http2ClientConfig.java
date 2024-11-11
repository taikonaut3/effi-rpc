package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.config.AbstractURLConfig;
import io.effi.rpc.core.config.ClientConfig;

/**
 * Configuration class for HTTP/2 client settings.
 */
public class Http2ClientConfig extends AbstractURLConfig implements ClientConfig {
    Http2ClientConfig(String name, URL url) {
        super(name, url);
    }

    /**
     * Creates and returns a new {@link Http2ClientConfigBuilder} instance for constructing
     * {@link Http2ClientConfig} objects using a fluent API.
     *
     * @return
     */
    public static Http2ClientConfigBuilder builder() {
        return new Http2ClientConfigBuilder();
    }
}
