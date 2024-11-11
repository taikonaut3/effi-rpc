package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.protocol.http.HttpCallerBuilder;

import static io.effi.rpc.common.constant.Component.Protocol.H2;

/**
 * Builder for creating {@link Http2Caller} instances,defining settings for caller.
 *
 * @param <T> The type of return.
 */
public class Http2CallerBuilder<T> extends HttpCallerBuilder<Http2Caller<T>, Http2CallerBuilder<T>> {

    public Http2CallerBuilder(TypeToken<T> returnType) {
        super(returnType);
    }


    @Override
    protected String protocol() {
        return  H2;
    }

    @Override
    protected Http2Caller<T> build(URL url) {
        return new Http2Caller<>(url, this);
    }
}
