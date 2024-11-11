package io.effi.rpc.protocol.http;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.protocol.support.AbstractCallee;
import io.effi.rpc.transport.http.HttpMethod;
import io.effi.rpc.transport.http.HttpVersion;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Standard Abstract Http Callee.
 *
 * @param <T>
 */
@Getter
@Accessors(fluent = true)
public abstract class HttpCallee<T> extends AbstractCallee<T> {

    protected HttpVersion version;

    protected HttpMethod httpMethod;

    protected HttpCallee(URL url, HttpCalleeBuilder<?, ?> builder) {
        super(url, builder);
        this.version = builder.version;
        this.httpMethod = builder.httpMethod;
    }

}
