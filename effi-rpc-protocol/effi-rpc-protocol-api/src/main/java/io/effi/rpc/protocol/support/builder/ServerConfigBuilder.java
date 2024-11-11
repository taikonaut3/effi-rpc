package io.effi.rpc.protocol.support.builder;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.ChainBuilder;
import io.effi.rpc.common.url.Parameter;
import io.effi.rpc.common.url.Parameterization;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.core.config.ServerConfig;

/**
 * Builder for configuring {@link ServerConfig} instances.
 *
 * @param <T> The type of {@link ServerConfig}.
 * @param <C> The type of the builder.
 */
public abstract class ServerConfigBuilder<T extends ServerConfig, C extends ServerConfigBuilder<T, C>>
        implements ChainBuilder<T, C>, Parameterization {

    /**
     * Server name.
     */
    protected String name;

    /**
     * Server protocol.
     */
    protected String protocol;

    /**
     * Whether SSL is enabled.
     */
    @Parameter(KeyConstant.SSL)
    protected boolean ssl = false;

    /**
     * Maximum number of unconnected clients.
     */
    @Parameter(KeyConstant.MAX_UN_CONNECTIONS)
    protected int maxUnConnections = Constant.DEFAULT_MAX_UN_CONNECTIONS;

    /**
     * Maximum number of threads available for server.
     */
    @Parameter(KeyConstant.MAX_THREADS)
    protected int maxThreads = Constant.DEFAULT_MAX_CPU_THREADS;

    /**
     * Maximum message size that the server can receive.
     */
    @Parameter(KeyConstant.MAX_RECEIVE_SIZE)
    protected int maxMessageSize = Constant.DEFAULT_MAX_MESSAGE_SIZE;

    /**
     * Keep-alive timeout in milliseconds.
     */
    @Parameter(KeyConstant.MAX_UN_CONNECTIONS)
    protected int keepAliveTimeout;

    /**
     * Sets the server name.
     *
     * @param name Server name.
     * @return This builder.
     */
    public C name(String name) {
        this.name = name;
        return returnChain();
    }

    /**
     * Enables or disables SSL for the server.
     *
     * @param ssl Whether SSL is enabled.
     * @return This builder.
     */
    public C ssl(boolean ssl) {
        this.ssl = ssl;
        return returnChain();
    }

    /**
     * Sets the maximum number of threads for the server.
     *
     * @param maxThreads Maximum number of threads.
     * @return This builder.
     */
    public C maxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return returnChain();
    }

    /**
     * Sets the maximum number of unconnected clients.
     *
     * @param maxUnConnections Maximum number of unconnected clients.
     * @return This builder.
     */
    public C maxUnConnections(int maxUnConnections) {
        this.maxUnConnections = maxUnConnections;
        return returnChain();
    }

    /**
     * Sets the maximum message size the server can receive.
     *
     * @param maxMessageSize Maximum message size.
     * @return This builder.
     */
    public C maxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
        return returnChain();
    }

    /**
     * Sets the keep-alive timeout for the server.
     *
     * @param keepAliveTimeout Keep-alive timeout in milliseconds.
     * @return This builder.
     */
    public C keepAliveTimeout(int keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
        return returnChain();
    }

    /**
     * Builds and returns the configured {@link ServerConfig} instance.
     *
     * @return Configured {@link ServerConfig} instance.
     */
    @Override
    public T build() {
        AssertUtil.notBlank(protocol, "protocol is blank");
        URL url = URL.builder()
                .type(URLType.SERVER)
                .protocol(protocol)
                .address(Constant.DEFAULT_ADDRESS)
                .params(parameterization())
                .build();
        return newServerConfig(url);
    }

    /**
     * Creates a new {@link ServerConfig} instance with the specified URL.
     *
     * @param url Configuration URL.
     * @return New {@link ServerConfig} instance.
     */
    protected abstract T newServerConfig(URL url);
}


