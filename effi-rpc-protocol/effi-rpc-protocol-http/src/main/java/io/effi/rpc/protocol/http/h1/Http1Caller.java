package io.effi.rpc.protocol.http.h1;

import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Caller;
import io.effi.rpc.protocol.http.HttpCaller;

/**
 * Http1.1 protocol implementation of {@link Caller}.
 *
 * @param <R>
 */
public class Http1Caller<R> extends HttpCaller<R> {

    Http1Caller(URL url, Http1CallerBuilder<R> builder) {
        super(url, builder);
    }

    /**
     * Creates and returns a new {@link Http1CallerBuilder} instance for constructing
     * {@link Http1Caller} objects using a fluent API.
     *
     * @param returnType
     * @param <R>
     * @return
     */
    public static <R> Http1CallerBuilder<R> builder(TypeToken<R> returnType) {
        return new Http1CallerBuilder<>(returnType);
    }

}
