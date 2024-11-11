package io.effi.rpc.transport.http;

import io.effi.rpc.common.extension.ChainBuilder;
import io.effi.rpc.common.url.URL;

/**
 * Build an HTTP envelope with configurable version, method, URL, and headers.
 *
 * @param <BODY> The type of the body of the HTTP envelope.
 * @param <T>    the type of objects being built.
 * @param <C>    the type of the implementing class that extends this interface,
 *               allowing for method chaining
 */
public abstract class HttpEnvelopeBuilder<BODY, T, C extends HttpEnvelopeBuilder<BODY, T, C>>
        extends DefaultHttpEnvelope<BODY> implements ChainBuilder<T, C> {

    /**
     * Sets the HTTP version for the envelope.
     *
     * @param version The HTTP version to set.
     * @return The current builder instance for chaining.
     */
    public C version(HttpVersion version) {
        this.version = version;
        return returnChain();
    }

    /**
     * Sets the HTTP method for the envelope.
     *
     * @param method The HTTP method to set (e.g., GET, POST).
     * @return The current builder instance for chaining.
     */
    public C method(HttpMethod method) {
        this.method = method;
        return returnChain();
    }

    /**
     * Sets the URL for the HTTP envelope.
     *
     * @param url The URL to set for the request.
     * @return The current builder instance for chaining.
     */
    public C url(URL url) {
        this.url = url;
        return returnChain();
    }

    /**
     * Sets the headers for the HTTP envelope.
     *
     * @param headers The headers to set for the request.
     * @return The current builder instance for chaining.
     */
    public C headers(HttpHeaders headers) {
        this.headers = headers;
        return returnChain();
    }
}

