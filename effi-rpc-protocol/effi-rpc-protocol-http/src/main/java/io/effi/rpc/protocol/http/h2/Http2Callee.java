package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.arg.MethodMapper;
import io.effi.rpc.protocol.http.HttpCallee;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Http2 protocol implementation of {@link io.effi.rpc.core.Callee}.
 *
 * @param <T>
 */
@Getter
@Accessors(fluent = true)
public class Http2Callee<T> extends HttpCallee<T> {

    Http2Callee(URL url, Http2CalleeBuilder<T> builder) {
        super(url, builder);
    }

    /**
     * Creates and returns a new {@link Http2CalleeBuilder} instance for constructing
     * {@link Http2Callee} objects using a fluent API.
     *
     * @param methodMapper
     * @param <T>
     * @return a new {@link Http2CalleeBuilder} instance
     */
    public static <T> Http2CalleeBuilder<T> builder(MethodMapper<T> methodMapper) {
        return new Http2CalleeBuilder<>(methodMapper);
    }

}
