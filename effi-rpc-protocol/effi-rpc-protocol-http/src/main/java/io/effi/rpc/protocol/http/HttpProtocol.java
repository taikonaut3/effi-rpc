package io.effi.rpc.protocol.http;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.DateUtil;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.ReceiveInvocation;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.protocol.http.codec.HttpClientCodec;
import io.effi.rpc.protocol.http.codec.HttpServerCodec;
import io.effi.rpc.protocol.protocol.AbstractProtocol;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.http.*;
import io.effi.rpc.transport.util.HttpUtil;

import java.time.LocalDateTime;

import static io.effi.rpc.transport.http.HttpHeaderNames.HOST;

/**
 * Abstract HttpProtocol.
 */
public class HttpProtocol extends AbstractProtocol<HttpRequest<Object>, HttpResponse<Object>> {

    private final HttpVersion version;

    public HttpProtocol(HttpVersion version) {
        super(Component.Protocol.HTTP);
        this.version = version;
        this.clientCodec = new HttpClientCodec(this);
        this.serverCodec = new HttpServerCodec(this);
    }

    @Override
    public <T> CallInvocation<T> createInvocation(ReplyFuture future, Caller<?> caller, Object[] args) {
        if (caller instanceof HttpCaller<?> httpCaller) {
            return new HttpCallInvocation<>(version, future, httpCaller, args);
        }
        throw new IllegalArgumentException("Unsupported caller type: " + ObjectUtil.simpleClassName(caller));
    }

    @Override
    public <T> ReceiveInvocation<T> createInvocation(Channel channel, URL requestUrl, Callee<?> callee, Object[] args) {
        if (callee instanceof HttpCallee<?> httpCallee) {
            return new HttpReceiveInvocation<>(version, channel, requestUrl, httpCallee, args);
        }
        throw new IllegalArgumentException("Unsupported callee type: " + ObjectUtil.simpleClassName(callee));
    }

    @Override
    public HttpRequest<Object> createRequest(CallInvocation<?> invocation) {
        var httpInvocation = (HttpCallInvocation<?>) invocation;
        URL url = httpInvocation.callerUrl();
        URL requestUrl = invocation.requestUrl();
        String timestamp = DateUtil.format(LocalDateTime.now());
        url.addParam(KeyConstant.TIMESTAMP, timestamp);
        HttpHeaders requestHeaders = httpInvocation.requestHeaders();
        requestHeaders.addIfAbsent(HOST, url.address());
        requestHeaders.addIfAbsent(VirtueHttpHeaderNames.CALLER_URL, url.toString());
        return HttpRequest.builder()
                .version(version)
                .method(httpInvocation.httpMethod())
                .url(requestUrl)
                .headers(requestHeaders)
                .body(findBody(invocation))
                .build();
    }

    @Override
    public HttpResponse<Object> createResponse(ReceiveInvocation<?> invocation, Result result) {
        int statusCode = 200;
        Object value = result.value();
        if (result.hasException()) {
            value = SERVER_INVOKE_EXCEPTION + result.exception().getMessage();
            statusCode = 500;
        }
        URL requestUrl = null;
        HttpMethod method = null;
        HttpHeaders responseHeaders;
        if (invocation != null) {
            var httpInvocation = (HttpReceiveInvocation<?>) invocation;
            responseHeaders = httpInvocation.responseHeaders();
            requestUrl = invocation.requestUrl();
            method = httpInvocation.httpMethod();
        } else {
            responseHeaders = new DefaultHttpHeaders();
            responseHeaders.add(HttpUtil.regularResponseHeaders());
            responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_TEXT.getName());
        }
        return HttpResponse.builder()
                .version(version)
                .method(method)
                .statusCode(statusCode)
                .url(requestUrl)
                .headers(responseHeaders)
                .body(value)
                .build();
    }

    /**
     * Returns http version.
     *
     * @return
     */
    public HttpVersion version() {
        return version;
    }

}
