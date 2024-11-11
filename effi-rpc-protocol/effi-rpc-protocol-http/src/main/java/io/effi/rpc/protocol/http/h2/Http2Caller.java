package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.protocol.http.HttpCaller;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Http2 protocol implementation of {@link Caller}.
 *
 * @param <R>
 */
@Getter
@Accessors(fluent = true)
public class Http2Caller<R> extends HttpCaller<R> {

    Http2Caller(URL url, Http2CallerBuilder<R> builder) {
        super(url, builder);
    }

    /**
     * Creates and returns a new {@link Http2CallerBuilder} instance for constructing
     * {@link Http2Caller} objects using a fluent API.
     *
     * @param returnType
     * @param <R>
     * @return
     */
    public static <R> Http2CallerBuilder<R> builder(TypeToken<R> returnType) {
        return new Http2CallerBuilder<>(returnType);
    }
}
