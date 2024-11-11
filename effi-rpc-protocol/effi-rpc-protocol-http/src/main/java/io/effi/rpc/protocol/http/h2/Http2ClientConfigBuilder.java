package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.Parameter;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.protocol.support.builder.ClientConfigBuilder;

import static io.effi.rpc.common.constant.Component.Protocol.H2;

/**
 * Builder for HTTP/2 client configuration.
 */
public class Http2ClientConfigBuilder extends ClientConfigBuilder<Http2ClientConfig, Http2ClientConfigBuilder> {

    /**
     * Maximum size of the HPACK header compression table.
     */
    @Parameter(KeyConstant.HEADER_TABLE_SIZE)
    private int headerTableSize = Constant.DEFAULT_MAX_HEADER_TABLE_SIZE;

    /**
     * Enables or disables server push.
     */
    @Parameter(KeyConstant.PUSH_ENABLED)
    private boolean pushEnabled = false;

    /**
     * Maximum number of concurrent streams allowed per connection.
     */
    @Parameter(KeyConstant.MAX_CONCURRENT_STREAMS)
    private int maxConcurrentStreams = Constant.DEFAULT_MAX_CONCURRENT_STREAMS;

    /**
     * Initial window size for flow control.
     */
    @Parameter(KeyConstant.INITIAL_WINDOW_SIZE)
    private int initialWindowSize = Constant.DEFAULT_INITIAL_WINDOW_SIZE;

    /**
     * Maximum size of a single HTTP/2 frame.
     */
    @Parameter(KeyConstant.MAX_FRAME_SIZE)
    private int maxFrameSize = Constant.DEFAULT_MAX_FRAME_SIZE;

    /**
     * Maximum size of the header list allowed.
     */
    @Parameter(KeyConstant.MAX_HEADER_LIST_SIZE)
    private int maxHeaderListSize = Constant.DEFAULT_MAX_HEADER_LIST_SIZE;

    /**
     * Constructor that sets the default protocol to HTTP/2 and enables SSL.
     */
    public Http2ClientConfigBuilder() {
        this.protocol = H2;
        ssl(true);
    }

    /**
     * Sets the size of the HPACK header compression table.
     *
     * @param headerTableSize the maximum header table size
     * @return the current builder instance for chaining
     */
    public Http2ClientConfigBuilder headerTableSize(int headerTableSize) {
        this.headerTableSize = headerTableSize;
        return returnChain();
    }

    /**
     * Sets the initial window size for HTTP/2 flow control.
     *
     * @param initialWindowSize the initial window size
     * @return the current builder instance for chaining
     */
    public Http2ClientConfigBuilder initialWindowSize(int initialWindowSize) {
        this.initialWindowSize = initialWindowSize;
        return returnChain();
    }

    /**
     * Sets the maximum number of concurrent streams allowed per connection.
     *
     * @param maxConcurrentStreams the maximum number of concurrent streams
     * @return the current builder instance for chaining
     */
    public Http2ClientConfigBuilder maxConcurrentStreams(int maxConcurrentStreams) {
        this.maxConcurrentStreams = maxConcurrentStreams;
        return returnChain();
    }

    /**
     * Sets the maximum size for HTTP/2 frames.
     *
     * @param maxFrameSize the maximum frame size
     * @return the current builder instance for chaining
     */
    public Http2ClientConfigBuilder maxFrameSize(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        return returnChain();
    }

    /**
     * Sets the maximum size for the HTTP/2 header list.
     *
     * @param maxHeaderListSize the maximum header list size
     * @return the current builder instance for chaining
     */
    public Http2ClientConfigBuilder maxHeaderListSize(int maxHeaderListSize) {
        this.maxHeaderListSize = maxHeaderListSize;
        return returnChain();
    }

    /**
     * Enables or disables HTTP/2 server push.
     *
     * @param pushEnabled true to enable server push, false to disable
     * @return the current builder instance for chaining
     */
    public Http2ClientConfigBuilder pushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
        return returnChain();
    }

    @Override
    protected Http2ClientConfig newClientConfig(URL url) {
        return new Http2ClientConfig(name, url);
    }
}
