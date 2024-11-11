package io.effi.rpc.protocol.support.builder;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.ChainBuilder;
import io.effi.rpc.common.url.Parameter;
import io.effi.rpc.common.url.Parameterization;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.config.RegistryConfig;

/**
 * Builder for configuring {@link RegistryConfig} instances.
 *
 * @param <T> The type of {@link RegistryConfig}.
 * @param <C> The type of the builder.
 */
public abstract class RegistryConfigBuilder<T extends RegistryConfig, C extends RegistryConfigBuilder<T, C>>
        implements ChainBuilder<T, C>, Parameterization {

    /**
     * Registry name.
     */
    protected String name;

    /**
     * Registry type (protocol).
     */
    protected String type;

    /**
     * Registry address.
     */
    protected String address;

    /**
     * Connection timeout for the registry.
     */
    @Parameter(KeyConstant.CONNECT_TIMEOUT)
    protected int connectTimeout = Constant.DEFAULT_CONNECT_TIMEOUT;

    /**
     * Session timeout for the registry.
     */
    @Parameter(KeyConstant.SESSION_TIMEOUT)
    protected int sessionTimeout = Constant.DEFAULT_SESSION_TIMEOUT;

    /**
     * Number of retry attempts for registry operations.
     */
    @Parameter(KeyConstant.RETRIES)
    protected int retries = Constant.DEFAULT_RETRIES;

    /**
     * Interval between retries for registry operations.
     */
    @Parameter(KeyConstant.RETRY_INTERVAL)
    protected int retryInterval = Constant.DEFAULT_INTERVAL;

    /**
     * Health check interval for the registry.
     */
    @Parameter(KeyConstant.HEALTH_CHECK_INTERVAL)
    protected int healthCheckInterval = Constant.DEFAULT_HEALTH_CHECK_INTERVAL;

    /**
     * Sets the registry name.
     *
     * @param name Registry name.
     * @return This builder.
     */
    public C name(String name) {
        this.name = name;
        return returnChain();
    }

    /**
     * Sets the registry URL and extracts type and address.
     *
     * @param url Registry URL.
     * @return This builder.
     */
    public C url(String url) {
        URL urlObj = URL.valueOf(url);
        this.type = urlObj.protocol();
        this.address = urlObj.address();
        return returnChain();
    }

    /**
     * Sets the connection timeout.
     *
     * @param connectTimeout Connection timeout in milliseconds.
     * @return This builder.
     */
    public C connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return returnChain();
    }

    /**
     * Sets the session timeout.
     *
     * @param sessionTimeout Session timeout in milliseconds.
     * @return This builder.
     */
    public C sessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return returnChain();
    }

    /**
     * Sets the number of retry attempts.
     *
     * @param retries Number of retries.
     * @return This builder.
     */
    public C retries(int retries) {
        this.retries = retries;
        return returnChain();
    }

    /**
     * Sets the retry interval.
     *
     * @param retryInterval Retry interval in milliseconds.
     * @return This builder.
     */
    public C retryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
        return returnChain();
    }

    /**
     * Sets the health check interval.
     *
     * @param healthCheckInterval Health check interval in milliseconds.
     * @return This builder.
     */
    public C healthCheckInterval(int healthCheckInterval) {
        this.healthCheckInterval = healthCheckInterval;
        return returnChain();
    }

    /**
     * Builds and returns the configured {@link RegistryConfig} instance.
     *
     * @return Configured {@link RegistryConfig} instance.
     */
    @Override
    public T build() {
        URL url = URL.builder()
                .protocol(type)
                .address(address)
                .params(parameterization())
                .build();
        if (name == null) {
            name = url.uri();
        }
        return newRegistryConfig(url);
    }

    /**
     * Creates a new {@link RegistryConfig} instance with the specified URL.
     *
     * @param url Configuration URL.
     * @return New {@link RegistryConfig} instance.
     */
    protected abstract T newRegistryConfig(URL url);
}


