package io.effi.rpc.protocol.http.h1;

import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.protocol.http.HttpCallerBuilder;

import static io.effi.rpc.common.constant.Component.Protocol.HTTP;

/**
 * Builder for creating {@link Http1Caller} instances,defining settings for caller.
 *
 * @param <T> The type of return.
 */
public class Http1CallerBuilder<T> extends HttpCallerBuilder<Http1Caller<T>, Http1CallerBuilder<T>> {

    public Http1CallerBuilder(TypeToken<T> returnType) {
        super(returnType);
    }

    @Override
    protected String protocol() {
        return HTTP;
    }

    @Override
    protected Http1Caller<T> build(URL url) {
        return new Http1Caller<>(url, this);
    }
}
