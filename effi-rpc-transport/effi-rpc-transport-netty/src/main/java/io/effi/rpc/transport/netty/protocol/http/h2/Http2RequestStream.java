package io.effi.rpc.transport.netty.protocol.http.h2;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.http.HttpMethod;
import io.netty.handler.codec.http2.Http2FrameStream;

/**
 * Generate full http2 request stream.
 */
public class Http2RequestStream extends NettyHttp2Stream {

    public Http2RequestStream(URL requestUrl, Http2FrameStream stream) {
        super(requestUrl, stream);
    }

    @Override
    public HttpMethod method() {
        CharSequence method = headers.method();
        return HttpMethod.valueOf(method.toString());
    }

    @Override
    protected void end() {
        super.end();
        CharSequence path = headers.path();
        url.set(HttpMethod.ATTRIBUTE_KEY, method());
        String queryPathStr = path.toString();
        url.path(queryPathStr);

    }

}
