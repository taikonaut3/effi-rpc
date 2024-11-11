package io.effi.rpc.protocol.http.codec;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.exception.UnSupportedDecodeException;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.common.util.bytes.ByteReader;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.protocol.http.HttpProtocol;
import io.effi.rpc.protocol.support.arg.ParamVarHandler;
import io.effi.rpc.protocol.support.arg.PathVarHandler;
import io.effi.rpc.transport.Response;
import io.effi.rpc.transport.codec.AbstractServerCodec;
import io.effi.rpc.transport.http.HttpRequest;
import io.effi.rpc.transport.http.HttpResponse;
import io.effi.rpc.transport.resolver.ArgumentResolver;
import io.effi.rpc.transport.resolver.BodyArgumentHandler;
import io.effi.rpc.transport.util.HttpUtil;

import java.io.IOException;

/**
 * HTTP server codec for encoding HTTP responses and decoding HTTP requests.
 * Implements {@link BodyArgumentHandler} to process request bodies and arguments.
 */
public class HttpServerCodec extends AbstractServerCodec<HttpResponse<Object>, HttpRequest<ByteReader>> implements BodyArgumentHandler<HttpRequest<ByteReader>> {

    public HttpServerCodec(HttpProtocol protocol) {
        this.protocol = protocol;
        this.argumentResolver = new ArgumentResolver<>() {
            @Override
            protected void initialize() {
                add(new ParamVarHandler<>(HttpRequest::url));
                add(new PathVarHandler<>(HttpRequest::url));
                add(HttpServerCodec.this);
            }
        };
    }

    @Override
    protected HttpResponse<byte[]> encodeResponse(Response response, HttpResponse<Object> httpResponse) throws Throwable {
        return HttpResponse.builder()
                .version(httpResponse.version())
                .method(httpResponse.method())
                .url(httpResponse.url())
                .statusCode(httpResponse.statusCode())
                .headers(httpResponse.headers())
                .body(HttpUtil.encodeBody(httpResponse))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected HttpRequest<ByteReader> decodeRequest(Object message) throws Throwable {
        if (message instanceof HttpRequest<?> httpRequest && httpRequest.body() instanceof ByteReader) {
            return (HttpRequest<ByteReader>) message;
        }
        throw new UnSupportedDecodeException("Unsupported message type " + ObjectUtil.simpleClassName(message));
    }

    @Override
    public Object handler(HttpRequest<ByteReader> request, Callee<?> callee, ParameterMapper wrapper) {
        Object body = null;
        try {
            body = HttpUtil.decodeBody(request, wrapper.parameter().getParameterizedType());
        } catch (IOException e) {
            throw RpcException.wrap(e);
        }
        request.body(body);
        return body;
    }
}
