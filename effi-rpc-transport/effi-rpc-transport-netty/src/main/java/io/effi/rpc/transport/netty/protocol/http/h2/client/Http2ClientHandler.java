/*
 * Copyright 2020 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.effi.rpc.transport.netty.protocol.http.h2.client;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.http.HttpRequest;
import io.effi.rpc.transport.http.HttpResponse;
import io.effi.rpc.transport.netty.ByteBufReader;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.NettySupport;
import io.effi.rpc.transport.netty.protocol.http.URLBinderChannelHandler;
import io.effi.rpc.transport.netty.protocol.http.h2.Http2ResponseStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http2.Http2DataFrame;
import io.netty.handler.codec.http2.Http2HeadersFrame;
import io.netty.handler.codec.http2.Http2StreamFrame;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * Handles HTTP/2 stream frame responses. This is a useful approach if you specifically want to check
 * the main HTTP/2 response DATA/HEADERs, but in this example it's used purely to see whether
 * our request (for a specific stream id) has had a final response (for that same stream id).
 */
@Sharable
public final class Http2ClientHandler extends URLBinderChannelHandler {

    private final InitializedConfig config;

    public Http2ClientHandler(InitializedConfig config) {
        this.config = config;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().addLast(config.handler());
    }

    @Override
    protected void writeHttpRequest(ChannelHandlerContext ctx, HttpRequest<byte[]> request, ChannelPromise promise) throws Exception {
        Http2StreamFrame[] frames = NettySupport.toHttp2StreamFrames(request);
        for (Http2StreamFrame frame : frames) {
            ctx.write(frame, ctx.newPromise());
        }
    }

    @Override
    protected void readHttpResponse(ChannelHandlerContext ctx, Object msg, URL requestUrl) throws Exception {
        Http2ResponseStream responseStream = null;
        if (msg instanceof Http2HeadersFrame headersFrame) {
            responseStream = onHeadersRead(ctx, headersFrame, requestUrl);
        } else if (msg instanceof Http2DataFrame dataFrame) {
            responseStream = onDataRead(ctx, dataFrame, requestUrl);
        } else {
            super.channelRead(ctx, msg);
        }
        if (responseStream != null && responseStream.endStream()) {
            HttpResponse<ByteBufReader> httpResponse = NettySupport.fromHttp2ResponseStream(responseStream);
            ctx.fireChannelRead(httpResponse);
            NettySupport.unbindURL(ctx.channel());
            NettySupport.removeHttp2ResponseStream(ctx, responseStream);
        }
    }

    /**
     * handle headers frame.
     *
     * @param ctx
     * @param headersFrame
     * @param requestUrl
     * @throws Exception
     */
    private Http2ResponseStream onHeadersRead(ChannelHandlerContext ctx, Http2HeadersFrame headersFrame, URL requestUrl) throws Exception {
        Http2ResponseStream responseStream = NettySupport.acquireHttp2ResponseStream(ctx, headersFrame.stream(), requestUrl);
        responseStream.parseHeaderFrame(headersFrame);
        return responseStream;

    }

    /**
     * Handle data frame.
     *
     * @param ctx
     * @param dataFrame
     * @param requestUrl
     * @throws Exception
     */
    private Http2ResponseStream onDataRead(ChannelHandlerContext ctx, Http2DataFrame dataFrame, URL requestUrl) throws Exception {
        Http2ResponseStream responseStream = NettySupport.acquireHttp2ResponseStream(ctx, dataFrame.stream(), requestUrl);
        responseStream.parseDataFrame(dataFrame);
        return responseStream;
    }
}
