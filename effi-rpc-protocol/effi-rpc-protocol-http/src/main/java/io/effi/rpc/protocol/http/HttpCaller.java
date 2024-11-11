package io.effi.rpc.protocol.http;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.protocol.support.caller.AbstractUnaryCaller;
import io.effi.rpc.transport.http.HttpMethod;
import io.effi.rpc.transport.http.HttpVersion;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Standard Http Caller implementation of {@link Caller}.
 *
 * @param <R>
 */
@Getter
@Accessors(fluent = true)
public abstract class HttpCaller<R> extends AbstractUnaryCaller<R> {

    protected HttpVersion version;

    protected HttpMethod httpMethod;

    protected HttpCaller(URL url, HttpCallerBuilder<?, ?> builder) {
        super(url, builder);
        this.version = builder.version;
        this.httpMethod = builder.httpMethod;
    }

}
