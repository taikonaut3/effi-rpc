package io.effi.rpc.protocol.support.builder;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.ChainBuilder;
import io.effi.rpc.common.url.Parameter;
import io.effi.rpc.common.url.Parameterization;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.core.config.ClientConfig;

/**
 * Builder for configuring {@link ClientConfig} instances with connection and protocol settings.
 *
 * @param <T> The type of {@link ClientConfig}.
 * @param <C> The type of the builder.
 */
public abstract class ClientConfigBuilder<T extends ClientConfig, C extends ClientConfigBuilder<T, C>>
        implements ChainBuilder<T, C>, Parameterization {

    /**
     * Client name identifier.
     */
    protected String name;

    /**
     * Protocol to be used by the client.
     */
    protected String protocol;

    /**
     * Enables SSL if set to true.
     */
    @Parameter(KeyConstant.SSL)
    protected boolean ssl = false;

    /**
     * Maximum number of connections allowed.
     */
    @Parameter(KeyConstant.MAX_CONNECTIONS)
    protected int maxConnections = Constant.DEFAULT_CLIENT_MAX_CONNECTIONS;

    /**
     * Maximum size for received messages.
     */
    @Parameter(KeyConstant.CLIENT_MAX_RECEIVE_SIZE)
    protected int maxMessageSize = Constant.DEFAULT_MAX_MESSAGE_SIZE;

    /**
     * Timeout for connection establishment, in milliseconds.
     */
    @Parameter(KeyConstant.CONNECT_TIMEOUT)
    protected int connectTimeout = Constant.DEFAULT_CONNECT_TIMEOUT;

    /**
     * Timeout for keep-alive connections, in milliseconds.
     */
    @Parameter(KeyConstant.KEEP_ALIVE_TIMEOUT)
    protected int keepAliveTimeout = Constant.DEFAULT_KEEP_ALIVE_TIMEOUT;

    /**
     * Times the client can remain idle before closing.
     */
    @Parameter(KeyConstant.SPARE_CLOSE_TIMES)
    protected int spareCloseTimes = Constant.DEFAULT_SPARE_CLOSE_TIMES;

    /**
     * Enables keep-alive if set to true.
     */
    @Parameter(KeyConstant.KEEPALIVE)
    protected boolean keepAlive = true;

    /**
     * Sets the client name.
     *
     * @param name Client name.
     * @return This builder.
     */
    public C name(String name) {
        this.name = name;
        return returnChain();
    }

    /**
     * Enables or disables SSL.
     *
     * @param ssl True to enable SSL.
     * @return This builder.
     */
    public C ssl(boolean ssl) {
        this.ssl = ssl;
        return returnChain();
    }

    /**
     * Sets the maximum allowed connections.
     *
     * @param maxConnections Maximum number of connections.
     * @return This builder.
     */
    public C maxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return returnChain();
    }

    /**
     * Sets the maximum size for received messages.
     *
     * @param maxMessageSize Maximum message size.
     * @return This builder.
     */
    public C maxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
        return returnChain();
    }

    /**
     * Sets the connection timeout.
     *
     * @param connectTimeout Timeout in milliseconds.
     * @return This builder.
     */
    public C connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return returnChain();
    }

    /**
     * Sets the timeout for keep-alive connections.
     *
     * @param keepAliveTimeout Keep-alive timeout in milliseconds.
     * @return This builder.
     */
    public C keepAliveTimeout(int keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
        return returnChain();
    }

    /**
     * Sets the idle times before closing spare connections.
     *
     * @param spareCloseTimes Number of idle times.
     * @return This builder.
     */
    public C spareCloseTimes(int spareCloseTimes) {
        this.spareCloseTimes = spareCloseTimes;
        return returnChain();
    }

    /**
     * Enables or disables keep-alive.
     *
     * @param keepAlive True to enable keep-alive.
     * @return This builder.
     */
    public C keepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return returnChain();
    }

    /**
     * Builds and returns a {@link ClientConfig} instance based on the provided parameters.
     *
     * @return Configured {@link ClientConfig} instance.
     */
    @Override
    public T build() {
        AssertUtil.notBlank(protocol, "protocol is blank");
        URL url = URL.builder()
                .type(URLType.CLIENT)
                .protocol(protocol)
                .address(Constant.DEFAULT_ADDRESS)
                .params(parameterization())
                .build();
        return newClientConfig(url);
    }

    /**
     * Creates a new {@link ClientConfig} instance with the specified URL.
     *
     * @param url Configured URL for the client.
     * @return New {@link ClientConfig} instance.
     */
    protected abstract T newClientConfig(URL url);
}
