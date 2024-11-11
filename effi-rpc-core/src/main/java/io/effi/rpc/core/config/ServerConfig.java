package io.effi.rpc.core.config;

/**
 * Configuration for server.
 *
 * @see URLConfig
 * @see io.effi.rpc.protocol.support.builder.ServerConfigBuilder
 */
public interface ServerConfig extends URLConfig {

    @Override
    default String managerKey() {
        return url().uri();
    }

}

