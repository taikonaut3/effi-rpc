package io.effi.rpc.transport.netty;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.netty.protocol.http.h2.Http2RequestStream;
import io.effi.rpc.transport.netty.protocol.http.h2.Http2ResponseStream;
import io.effi.rpc.transport.netty.protocol.http.h2.NettyHttp2Stream;
import io.effi.rpc.transport.util.HttpUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import static io.netty.handler.codec.http.DefaultHttpHeadersFactory.trailersFactory;

/**
 * Netty support.
 */
public class NettySupport {

    public static final AttributeKey<Http2StreamChannelBootstrap> H2_STREAM_BOOTSTRAP_KEY = AttributeKey.valueOf("h2-stream-bootstrap");

    private static final AttributeKey<URL> URL_KEY = AttributeKey.valueOf(KeyConstant.URL);

    private static final io.effi.rpc.common.extension.AttributeKey<Http2FrameStream> STREAM_KEY = io.effi.rpc.common.extension.AttributeKey.valueOf("stream");

    private static final String REQUEST_STREAM_PREFIX = "request-stream-";

    private static final String RESPONSE_STREAM_PREFIX = "response-stream-";

    /**
     * Acquires http2 request stream from channel,Create if it doesn't exist.
     *
     * @param ctx
     * @param stream
     * @param endpointUrl
     * @return
     */
    public static Http2RequestStream acquireHttp2RequestStream(ChannelHandlerContext ctx, Http2FrameStream stream, URL endpointUrl) {
        URL url = URL.builder()
                .type(URLType.REQUEST)
                .protocol(endpointUrl.protocol())
                .address(endpointUrl.address())
                .build();
        String streamKey = REQUEST_STREAM_PREFIX + stream.id();
        return acquireStream(streamKey, ctx, stream, url, Http2RequestStream::new);
    }

    /**
     * Acquires http2 response stream from channel,Create if it doesn't exist.
     *
     * @param ctx
     * @param stream
     * @param requestUrl
     * @return
     */
    public static Http2ResponseStream acquireHttp2ResponseStream(ChannelHandlerContext ctx, Http2FrameStream stream, URL requestUrl) {
        String streamKey = RESPONSE_STREAM_PREFIX + stream.id();
        return acquireStream(streamKey, ctx, stream, requestUrl, Http2ResponseStream::new);
    }

    /**
     * Removes http2 request stream from channel.
     *
     * @param ctx
     * @param requestStream
     */
    public static void removeHttp2RequestStream(ChannelHandlerContext ctx, Http2RequestStream requestStream) {
        removeStream(ctx, REQUEST_STREAM_PREFIX + requestStream.stream().id());
    }

    /**
     * Removes http2 response stream from channel.
     *
     * @param ctx
     * @param responseStream
     */
    public static void removeHttp2ResponseStream(ChannelHandlerContext ctx, Http2ResponseStream responseStream) {
        removeStream(ctx, RESPONSE_STREAM_PREFIX + responseStream.stream().id());
    }

    private static <T extends NettyHttp2Stream> T acquireStream(String streamKey, ChannelHandlerContext ctx, Http2FrameStream stream,
                                                                URL url, BiFunction<URL, Http2FrameStream, T> creator) {
        AttributeKey<T> streamAttributeKey = AttributeKey.valueOf(streamKey);
        Attribute<T> streamAttribute = ctx.channel().attr(streamAttributeKey);
        T nettyHttp2Stream = streamAttribute.get();
        if (nettyHttp2Stream == null) {
            nettyHttp2Stream = creator.apply(url, stream);
            streamAttribute.set(nettyHttp2Stream);
        }
        return nettyHttp2Stream;
    }

    private static void removeStream(ChannelHandlerContext ctx, String streamKey) {
        AttributeKey<NettyHttp2Stream> streamAttributeKey = AttributeKey.valueOf(streamKey);
        Attribute<NettyHttp2Stream> streamMessageAttribute = ctx.channel().attr(streamAttributeKey);
        streamMessageAttribute.set(null);
    }

    /**
     * Converts http2 headers and data to http2 stream frames.
     *
     * @param headers
     * @param data
     * @return
     */
    public static Http2StreamFrame[] toHttp2StreamFrames(Http2Headers headers, ByteBuf data) {
        boolean headersEndStream = !data.isReadable();
        Http2StreamFrame headersFrame = new DefaultHttp2HeadersFrame(headers, headersEndStream);
        Http2StreamFrame dataFrame = headersEndStream ? null : new DefaultHttp2DataFrame(data, true);
        return headersEndStream ? new Http2StreamFrame[]{headersFrame} : new Http2StreamFrame[]{headersFrame, dataFrame};
    }

    /**
     * Binds url to current channel.
     *
     * @param url
     * @param channel
     */
    public static void bindURL(URL url, Channel channel) {
        channel.attr(URL_KEY).set(url);
    }

    /**
     * Removes url from current channel.
     *
     * @param channel
     */
    public static void unbindURL(Channel channel) {
        Attribute<URL> attr = channel.attr(URL_KEY);
        attr.set(null);
    }

    /**
     * Binds http2 stream to request url.
     *
     * @param stream
     * @param requestUrl
     */
    public static void bindStream(Http2FrameStream stream, URL requestUrl) {
        requestUrl.set(STREAM_KEY, stream);
    }

    /**
     * Unbinds http2 stream from request url.
     *
     * @param requestUrl
     * @return
     */
    public static Http2FrameStream unbindStream(URL requestUrl) {
        Http2FrameStream http2FrameStream = requestUrl.get(STREAM_KEY);
        requestUrl.remove(STREAM_KEY);
        return http2FrameStream;
    }

    /**
     * Acquires url from current channel.
     *
     * @param channel
     * @return
     */
    public static URL acquireBoundChannel(Channel channel) {
        return channel.attr(URL_KEY).get();
    }

    /**
     * Gets ssl bytes.
     *
     * @param systemDir
     * @param defaultPath
     * @return
     * @throws Exception
     */
    public static byte[] getSslBytes(String systemDir, String defaultPath) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String caPath = System.getProperty(systemDir);
        InputStream caStream = null;
        try {
            if (StringUtil.isBlank(caPath)) {
                caStream = classLoader.getResourceAsStream(defaultPath);
            } else {
                caStream = new FileInputStream(caPath);
            }
            if (caStream != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = caStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                return outputStream.toByteArray();
            }
            return null;
        } finally {
            if (caStream != null) {
                caStream.close();
            }
        }
    }

    /**
     * Converts byte array to InputStream.
     *
     * @param bytes
     * @return
     */
    public static InputStream readBytes(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes can't null");
        }
        return new ByteArrayInputStream(bytes);
    }

    /**
     * Converts ByteBuf to byte array.
     *
     * @param buf
     * @return
     */
    public static byte[] getBytes(ByteBuf buf) {
        return io.netty.buffer.ByteBufUtil.getBytes(buf, buf.readerIndex(), buf.readableBytes(), false);
    }

    /**
     * Creates http2 stream channel.
     *
     * @param bootstrap
     * @param endpointUrl
     * @param connectTimeout
     * @return
     */
    public static NettyChannel acquireStreamChannel(Http2StreamChannelBootstrap bootstrap, URL endpointUrl, int connectTimeout) {
        try {
            Http2StreamChannel streamChannel = bootstrap.open().get(connectTimeout, TimeUnit.MILLISECONDS);
            return NettyChannel.acquire(streamChannel, endpointUrl);
        } catch (Exception e) {
            throw new RpcException("Acquire Http2StreamChannel fail", e);
        }
    }

    /**
     * Registers write exception handler.
     *
     * @param promise
     * @param handler
     * @param channel
     */
    public static void registerWriteExceptionHandler(ChannelPromise promise, ChannelHandler handler, io.effi.rpc.transport.channel.Channel channel) {
        promise.addListener(future -> {
            if (!future.isSuccess()) {
                handler.caught(channel, future.cause());
            }
        });
    }

    /**
     * Converts to netty's http headers.
     *
     * @param headers
     * @return
     */
    public static HttpHeaders toHttpHeaders(io.effi.rpc.transport.http.HttpHeaders headers) {
        DefaultHttpHeaders httpHeaders = new DefaultHttpHeaders();
        headers.forEach(item -> httpHeaders.add(item.getKey(), item.getValue()));
        return httpHeaders;
    }

    /**
     * Converts to netty's http2 headers.
     *
     * @param headers
     * @return
     */
    public static Http2Headers toHttp2Headers(io.effi.rpc.transport.http.HttpHeaders headers) {
        DefaultHttp2Headers httpHeaders = new DefaultHttp2Headers();
        headers.forEach(item -> httpHeaders.add(item.getKey(), item.getValue()));
        return httpHeaders;
    }

    /**
     * Converts from netty's http headers.
     *
     * @param headers
     * @return
     */
    public static io.effi.rpc.transport.http.HttpHeaders fromHttpHeaders(HttpHeaders headers) {
        io.effi.rpc.transport.http.DefaultHttpHeaders httpHeaders = new io.effi.rpc.transport.http.DefaultHttpHeaders();
        headers.forEach(item -> httpHeaders.add(item.getKey(), item.getValue()));
        return httpHeaders;
    }

    /**
     * Converts from netty's http2 headers.
     *
     * @param headers
     * @return
     */
    public static io.effi.rpc.transport.http.HttpHeaders fromHttp2Headers(Http2Headers headers) {
        io.effi.rpc.transport.http.DefaultHttpHeaders httpHeaders = new io.effi.rpc.transport.http.DefaultHttpHeaders();
        headers.forEach(item -> httpHeaders.add(item.getKey().toString(), item.getValue().toString()));
        return httpHeaders;
    }

    /**
     * Converts to netty's http method.
     *
     * @param httpMethod
     * @return
     */
    public static HttpMethod toHttpMethod(io.effi.rpc.transport.http.HttpMethod httpMethod) {
        return HttpMethod.valueOf(httpMethod.name());
    }

    /**
     * Converts from netty's http method.
     *
     * @param httpMethod
     * @return
     */
    public static io.effi.rpc.transport.http.HttpMethod fromHttpMethod(HttpMethod httpMethod) {
        return io.effi.rpc.transport.http.HttpMethod.valueOf(httpMethod.name());
    }

    /**
     * Converts to netty's full http request.
     *
     * @param request
     * @return
     */
    public static FullHttpRequest toFullHttpRequest(io.effi.rpc.transport.http.HttpRequest<byte[]> request) {
        URL requestUrl = request.url();
        return new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1,
                toHttpMethod(request.method()),
                requestUrl.queryPath(),
                Unpooled.wrappedBuffer(request.body()),
                toHttpHeaders(request.headers()),
                trailersFactory().newHeaders()
        );
    }

    /**
     * Converts to netty's full http response.
     *
     * @param response
     * @return
     */
    public static FullHttpResponse toFullHttpResponse(io.effi.rpc.transport.http.HttpResponse<byte[]> response) {
        return new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.valueOf(response.statusCode()),
                Unpooled.wrappedBuffer(response.body()),
                toHttpHeaders(response.headers()),
                trailersFactory().newHeaders()
        );
    }

    /**
     * Converts from netty's full http request.
     *
     * @param endpointUrl
     * @param request
     * @return
     */
    public static io.effi.rpc.transport.http.HttpRequest<ByteBufReader> fromFullHttpRequest(URL endpointUrl, FullHttpRequest request) {
        return io.effi.rpc.transport.http.HttpRequest.builder()
                .version(io.effi.rpc.transport.http.HttpVersion.HTTP_1_1)
                .method(fromHttpMethod(request.method()))
                .url(buildRequestUrl(endpointUrl, request))
                .headers(fromHttpHeaders(request.headers()))
                .body(new ByteBufReader(request.content()))
                .build();
    }

    /**
     * Converts from netty's full http response.
     *
     * @param response
     * @param requestUrl
     * @return
     */
    public static io.effi.rpc.transport.http.HttpResponse<ByteBufReader> fromFullHttpResponse(FullHttpResponse response, URL requestUrl) {
        return io.effi.rpc.transport.http.HttpResponse.builder()
                .version(io.effi.rpc.transport.http.HttpVersion.HTTP_1_1)
                .method(io.effi.rpc.transport.http.HttpMethod.getOf(requestUrl))
                .statusCode(response.status().code())
                .url(requestUrl)
                .headers(fromHttpHeaders(response.headers()))
                .body(new ByteBufReader(response.content()))
                .build();
    }

    /**
     * Converts to netty's http2 stream frames.
     *
     * @param request
     * @return
     */
    public static Http2StreamFrame[] toHttp2StreamFrames(io.effi.rpc.transport.http.HttpRequest<byte[]> request) {
        URL url = request.url();
        // build http2 headers
        Http2Headers http2Headers = toHttp2Headers(request.headers());
        http2Headers.scheme(HttpUtil.toStandardScheme(url));
        http2Headers.method(toHttpMethod(request.method()).name());
        http2Headers.path(url.queryPath());
        // wrapper http2 body
        ByteBuf data = Unpooled.wrappedBuffer(request.body());
        return toHttp2StreamFrames(http2Headers, data);
    }

    /**
     * Converts to netty's http2 stream frames.
     *
     * @param response
     * @return
     */
    public static Http2StreamFrame[] toHttp2StreamFrames(io.effi.rpc.transport.http.HttpResponse<byte[]> response) {
        // build http2 headers
        Http2Headers http2Headers = toHttp2Headers(response.headers());
        http2Headers.status(HttpResponseStatus.valueOf(response.statusCode()).codeAsText());
        // wrapper http2 body
        ByteBuf data = Unpooled.wrappedBuffer(response.body());
        return toHttp2StreamFrames(http2Headers, data);
    }

    /**
     * Converts from netty's http2 stream.
     *
     * @param responseStream
     * @return
     */
    public static io.effi.rpc.transport.http.HttpResponse<ByteBufReader> fromHttp2ResponseStream(Http2ResponseStream responseStream) {
        return io.effi.rpc.transport.http.HttpResponse.builder()
                .version(io.effi.rpc.transport.http.HttpVersion.HTTP_2_0)
                .method(responseStream.method())
                .statusCode(responseStream.statusCode())
                .url(responseStream.url())
                .headers(fromHttp2Headers(responseStream.headers()))
                .body(new ByteBufReader(responseStream.body()))
                .build();
    }

    /**
     * Converts from netty's http2 stream.
     *
     * @param requestStream
     * @return
     */
    public static io.effi.rpc.transport.http.HttpRequest<ByteBufReader> fromHtt2RequestStream(Http2RequestStream requestStream) {
        return io.effi.rpc.transport.http.HttpRequest.builder()
                .version(io.effi.rpc.transport.http.HttpVersion.HTTP_2_0)
                .method(requestStream.method())
                .url(requestStream.url())
                .headers(fromHttp2Headers(requestStream.headers()))
                .body(new ByteBufReader(requestStream.body()))
                .build();
    }

    private static URL buildRequestUrl(URL endpointUrl, FullHttpRequest httpRequest) {
        URL requestUrl = URL.builder()
                .type(URLType.REQUEST)
                .protocol(endpointUrl.protocol())
                .address(endpointUrl.address())
                .path(httpRequest.uri())
                .build();
        requestUrl.addParam(KeyConstant.ONEWAY, Boolean.FALSE.toString());
        return requestUrl;
    }
}
