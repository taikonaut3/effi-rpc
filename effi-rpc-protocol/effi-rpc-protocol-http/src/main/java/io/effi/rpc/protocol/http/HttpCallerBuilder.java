package io.effi.rpc.protocol.http;

import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.protocol.support.builder.CallerBuilder;
import io.effi.rpc.transport.http.DefaultHttpHeaders;
import io.effi.rpc.transport.http.HttpHeaders;
import io.effi.rpc.transport.http.HttpMethod;
import io.effi.rpc.transport.http.HttpVersion;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * Builder for creating {@link HttpCaller} instances,defining settings for callee.
 *
 * @param <T> The type of {@link HttpCaller}.
 * @param <C> The type of the builder.
 */
@Getter
@Accessors(fluent = true)
public abstract class HttpCallerBuilder<T extends HttpCaller<?>, C extends HttpCallerBuilder<T, C>>
        extends CallerBuilder<T, C> {

    /**
     * The HTTP version for the caller.
     */
    protected HttpVersion version = HttpVersion.HTTP_1_1;

    /**
     * The HTTP method for the callee (e.g., GET, POST).
     */
    protected HttpMethod httpMethod = HttpMethod.GET;

    /**
     * The headers to be sent in the HTTP request.
     */
    protected volatile HttpHeaders requestHeaders;

    protected HttpCallerBuilder(TypeToken<?> returnType) {
        super(returnType);
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
     * Adds multiple request headers to the callee.
     *
     * @param headers The headers to be added.
     */
    public C addRequestHeaders(Map<CharSequence, CharSequence> headers) {
        if (CollectionUtil.isNotEmpty(headers)) {
            delayedRequestHeaders().add(headers);
        }
        return returnChain();
    }

    /**
     * Adds a single request header to the caller.
     *
     * @param key   The header key.
     * @param value The header value.
     */
    public C addRequestHeader(CharSequence key, CharSequence value) {
        if (!StringUtil.isBlank(key) && !StringUtil.isBlank(value)) {
            delayedRequestHeaders().add(key, value);
        }
        return returnChain();
    }

    /**
     * Lazily initializes the request headers if they are not already set.
     * This ensures that the headers are only created when needed, improving performance.
     *
     * @return The HttpHeaders object that holds the request headers.
     */
    protected HttpHeaders delayedRequestHeaders() {
        if (requestHeaders == null) {
            synchronized (this) {
                if (requestHeaders == null) {
                    requestHeaders = new DefaultHttpHeaders();
                }
            }
        }
        return requestHeaders;
    }

}
