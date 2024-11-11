package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.arg.MethodMapper;
import io.effi.rpc.protocol.http.HttpCalleeBuilder;
import io.effi.rpc.protocol.http.h1.Http1Callee;

import static io.effi.rpc.common.constant.Component.Protocol.H2;

/**
 * Builder for creating {@link Http1Callee} instances,defining settings for callee.
 *
 * @param <T> The type of service.
 */
public class Http2CalleeBuilder<T> extends HttpCalleeBuilder<Http2Callee<T>, Http2CalleeBuilder<T>> {
    public Http2CalleeBuilder(MethodMapper<?> methodMapper) {
        super(methodMapper);
    }

    @Override
    protected String protocol() {
        return H2;
    }

    @Override
    protected Http2Callee<T> build(URL url) {
        return new Http2Callee<>(url, this);
    }
}
