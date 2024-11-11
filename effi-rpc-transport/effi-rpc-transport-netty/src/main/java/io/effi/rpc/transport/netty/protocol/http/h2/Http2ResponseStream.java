package io.effi.rpc.transport.netty.protocol.http.h2;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.Http2FrameStream;

/**
 * Generate full http2 response stream.
 */
public class Http2ResponseStream extends NettyHttp2Stream {

    public Http2ResponseStream(URL url, Http2FrameStream stream) {
        super(url, stream);
    }

    @Override
    public HttpMethod method() {
        return HttpMethod.getOf(url);
    }

    /**
     * Returns status code from response headers.
     *
     * @return
     */
    public int statusCode() {
        CharSequence status = headers.status();
        return HttpResponseStatus.parseLine(status).code();
    }

}
