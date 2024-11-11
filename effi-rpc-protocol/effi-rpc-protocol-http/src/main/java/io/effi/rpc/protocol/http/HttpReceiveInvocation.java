package io.effi.rpc.protocol.http;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.protocol.support.AbstractReceiveInvocation;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.http.DefaultHttpHeaders;
import io.effi.rpc.transport.http.HttpHeaders;
import io.effi.rpc.transport.http.HttpMethod;
import io.effi.rpc.transport.http.HttpVersion;
import io.effi.rpc.transport.util.HttpUtil;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * Http implementation of {@link io.effi.rpc.core.ReceiveInvocation}.
 *
 * @param <T>
 */
@Getter
@Accessors(fluent = true)
public class HttpReceiveInvocation<T> extends AbstractReceiveInvocation<T> {

    private static final Map<CharSequence, CharSequence> RESPONSE_REQUEST_HEADERS = HttpUtil.regularResponseHeaders();

    private final HttpVersion version;

    private final HttpMethod httpMethod;

    private final HttpHeaders responseHeaders;

    public HttpReceiveInvocation(HttpVersion version, Channel channel, URL requestUrl, HttpCallee<?> callee, Object[] args) {
        super(channel, requestUrl, callee, args);
        this.version = version;
        this.httpMethod = callee.httpMethod();
        this.responseHeaders = new DefaultHttpHeaders();
        HttpUtil.setContentType(this.responseHeaders, callee.url());
    }
}
