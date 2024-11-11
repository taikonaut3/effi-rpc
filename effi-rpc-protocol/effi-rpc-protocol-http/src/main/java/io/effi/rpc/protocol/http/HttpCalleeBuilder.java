package io.effi.rpc.protocol.http;

import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.arg.MethodMapper;
import io.effi.rpc.protocol.support.builder.CalleeBuilder;
import io.effi.rpc.transport.http.DefaultHttpHeaders;
import io.effi.rpc.transport.http.HttpHeaders;
import io.effi.rpc.transport.http.HttpMethod;
import io.effi.rpc.transport.http.HttpVersion;

import java.util.Map;

/**
 * Builder for creating {@link HttpCallee} instances,defining settings for callee.
 *
 * @param <T> The type of {@link HttpCallee}.
 * @param <C> The type of the builder.
 */
public abstract class HttpCalleeBuilder<T extends HttpCallee<?>, C extends HttpCalleeBuilder<T, C>>
        extends CalleeBuilder<T, C> {

    /**
     * The HTTP version for the callee.
     */
    protected HttpVersion version = HttpVersion.HTTP_1_1;

    /**
     * The HTTP method for the callee (e.g., GET, POST).
     */
    protected HttpMethod httpMethod = HttpMethod.GET;

    /**
     * The headers to be sent in the HTTP response.
     */
    protected volatile HttpHeaders responseHeaders;

    protected HttpCalleeBuilder(MethodMapper<?> methodMapper) {
        super(methodMapper);
    }

    /**
     * Sets the HTTP version for the callee.
     *
     * @param version The HTTP version to set (e.g., HTTP/1.1, HTTP/2).
     * @return This builder instance for fluent chaining.
     */
    public C version(HttpVersion version) {
        this.version = version;
        return returnChain();
    }

    /**
     * Sets the HTTP method for the callee.
     *
     * @param method The HTTP method to set (e.g., GET, POST).
     * @return This builder instance for fluent chaining.
     */
    public C method(HttpMethod method) {
        this.httpMethod = method;
        return returnChain();
    }

    /**
     * Adds multiple response headers to the callee.
     *
     * @param headers The headers to be added.
     */
    public void addResponseHeaders(Map<CharSequence, CharSequence> headers) {
        if (CollectionUtil.isNotEmpty(headers)) {
            delayedResponseHeaders().add(headers);
        }
    }

    /**
     * Adds a single response header to the callee.
     *
     * @param key   The header key.
     * @param value The header value.
     */
    public void addResponseHeader(CharSequence key, CharSequence value) {
        if (!StringUtil.isBlank(key) && !StringUtil.isBlank(value)) {
            delayedResponseHeaders().add(key, value);
        }
    }

    /**
     * Lazily initializes the response headers if they are not already set.
     * This ensures that the headers are only created when needed, improving performance.
     *
     * @return The HttpHeaders object that holds the response headers.
     */
    protected HttpHeaders delayedResponseHeaders() {
        if (responseHeaders == null) {
            synchronized (this) {
                if (responseHeaders == null) {
                    responseHeaders = new DefaultHttpHeaders();
                }
            }
        }
        return responseHeaders;
    }
}
