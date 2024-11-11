package io.effi.rpc.protocol.http.h1;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.arg.MethodMapper;
import io.effi.rpc.protocol.http.HttpCalleeBuilder;

import static io.effi.rpc.common.constant.Component.Protocol.HTTP;

/**
 * Builder for creating {@link Http1Callee} instances,defining settings for callee.
 *
 * @param <T> The type of service.
 */
public class Http1CalleeBuilder<T> extends HttpCalleeBuilder<Http1Callee<T>, Http1CalleeBuilder<T>> {
    public Http1CalleeBuilder(MethodMapper<T> methodMapper) {
        super(methodMapper);
    }

    @Override
    protected String protocol() {
        return HTTP;
    }

    @Override
    protected Http1Callee<T> build(URL url) {
        return new Http1Callee<>(url, this);
    }
}
