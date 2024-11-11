package io.effi.rpc.protocol.http;

import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.protocol.support.caller.AbstractCallInvocation;
import io.effi.rpc.transport.http.DefaultHttpHeaders;
import io.effi.rpc.transport.http.HttpHeaders;
import io.effi.rpc.transport.http.HttpMethod;
import io.effi.rpc.transport.http.HttpVersion;
import io.effi.rpc.transport.util.HttpUtil;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * Http implementation of {@link io.effi.rpc.core.CallInvocation}.
 *
 * @param <T>
 */
@Getter
@Accessors(fluent = true)
public class HttpCallInvocation<T> extends AbstractCallInvocation<T> {

    private static final Map<CharSequence, CharSequence> REGULAR_REQUEST_HEADERS = HttpUtil.regularRequestHeaders();

    private final HttpVersion version;

    private final HttpMethod httpMethod;

    private final HttpHeaders requestHeaders;

    protected HttpCallInvocation(HttpVersion version, ReplyFuture future, HttpCaller<?> caller, Object[] args) {
        super(future, caller, args);
        this.version = version;
        this.httpMethod = caller.httpMethod();
        this.requestHeaders = new DefaultHttpHeaders();
        this.requestHeaders.add(REGULAR_REQUEST_HEADERS);
        HttpUtil.setContentType(this.requestHeaders, caller.url());
    }

    private void setHttpMethod() {
        requestUrl.set(HttpMethod.ATTRIBUTE_KEY, httpMethod);
    }

}
