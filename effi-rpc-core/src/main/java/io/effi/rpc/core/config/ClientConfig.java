package io.effi.rpc.core.config;

import io.effi.rpc.common.util.StringUtil;

/**
 * Configuration for client.
 *
 * @see URLConfig
 * @see io.effi.rpc.protocol.support.builder.ClientConfigBuilder
 */
public interface ClientConfig extends URLConfig {

    @Override
    default String managerKey() {
        return StringUtil.isBlankOrDefault(name(), url().protocol());
    }
}


