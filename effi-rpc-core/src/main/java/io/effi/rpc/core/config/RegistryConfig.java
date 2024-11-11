package io.effi.rpc.core.config;

/**
 * Configuration for registry.
 *
 * @see URLConfig
 * @see io.effi.rpc.protocol.support.builder.RegistryConfigBuilder
 */
public interface RegistryConfig extends URLConfig {
    @Override
    default String managerKey() {
        return name();
    }
}
