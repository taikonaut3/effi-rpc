package io.effi.rpc.protocol.http.h1;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.arg.MethodMapper;
import io.effi.rpc.protocol.http.HttpCallee;

/**
 * Http1.1 protocol implementation of {@link io.effi.rpc.core.Callee}.
 *
 * @param <T>
 */
public class Http1Callee<T> extends HttpCallee<T> {

    Http1Callee(URL url, Http1CalleeBuilder<T> builder) {
        super(url, builder);
    }

    /**
     * Creates and returns a new {@link Http1CalleeBuilder} instance for constructing
     * {@link Http1Callee} objects using a fluent API.
     *
     * @param methodMapper
     * @param <T>
     * @return a new {@link Http1CalleeBuilder} instance
     */
    public static <T> Http1CalleeBuilder<T> builder(MethodMapper<T> methodMapper) {
        return new Http1CalleeBuilder<>(methodMapper);
    }

}
