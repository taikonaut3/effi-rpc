package io.effi.rpc.protocol.http.codec;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.exception.UnSupportedDecodeException;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.common.util.bytes.ByteReader;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.protocol.http.HttpProtocol;
import io.effi.rpc.transport.Request;
import io.effi.rpc.transport.codec.AbstractClientCodec;
import io.effi.rpc.transport.http.HttpRequest;
import io.effi.rpc.transport.http.HttpResponse;
import io.effi.rpc.transport.resolver.ReplyResolver;
import io.effi.rpc.transport.util.HttpUtil;

import java.io.IOException;

/**
 * HTTP client codec for encoding HTTP requests and decoding HTTP responses.
 * Implements {@link ReplyResolver} to resolve the response and provide the
 * final result.
 */
public class HttpClientCodec extends AbstractClientCodec<HttpRequest<Object>, HttpResponse<ByteReader>> implements ReplyResolver<HttpResponse<ByteReader>> {

    private final HttpProtocol protocol;

    public HttpClientCodec(HttpProtocol protocol) {
        this.protocol = protocol;
        this.replyResolver = this;
    }

    @Override
    protected HttpRequest<byte[]> encodeRequest(Request request, HttpRequest<Object> httpRequest) throws IOException {
        return HttpRequest.builder()
                .version(httpRequest.version())
                .method(httpRequest.method())
                .url(httpRequest.url())
                .headers(httpRequest.headers())
                .body(HttpUtil.encodeBody(httpRequest))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected HttpResponse<ByteReader> decodeResponse(Object message) {
        if (message instanceof HttpResponse<?> httpResponse && httpResponse.body() instanceof ByteReader) {
            return (HttpResponse<ByteReader>) message;
        }
        throw new UnSupportedDecodeException("Unsupported message type " + ObjectUtil.simpleClassName(message));
    }

    @Override
    public Result resolve(HttpResponse<ByteReader> response, Caller<?> caller) throws IOException {
        Object replyValue = HttpUtil.decodeBody(response, caller.returnType().type());
        response.body(replyValue);
        Result result = null;
        if (response.statusCode() != 200) {
            result = new Result(response.url(), new RpcException(String.valueOf(replyValue)));
        } else {
            result = new Result(response.url(), replyValue);
        }
        return result;
    }
}
