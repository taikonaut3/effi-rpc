package io.effi.rpc.transport.util;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.constant.Platform;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.common.util.bytes.ByteReader;
import io.effi.rpc.serialization.Serializer;
import io.effi.rpc.transport.http.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

import static io.effi.rpc.common.constant.Component.Compression.*;
import static io.effi.rpc.common.constant.Component.Protocol.*;
import static io.effi.rpc.transport.http.HttpHeaderNames.CONTENT_TYPE;

/**
 * Utility class for handling HTTP-related operations and transformations.
 */
public final class HttpUtil {

    private static final String IDENTIFY = "effi-rpc/" + Platform.virtueVersion();

    private static final String ACCEPT_TYPE = String.join(",", Arrays.stream(MediaType.values()).map(MediaType::getName).toList());

    /**
     * Creates common headers for client requests.
     *
     * @return A map of headers typically used in HTTP client requests.
     */
    public static Map<CharSequence, CharSequence> regularRequestHeaders() {
        String acceptEncoding = String.join(",", GZIP, DEFLATE, LZ4, SNAPPY);
        return Map.of(
                HttpHeaderNames.ACCEPT_ENCODING, acceptEncoding,
                HttpHeaderNames.ACCEPT, ACCEPT_TYPE,
                HttpHeaderNames.USER_AGENT, IDENTIFY
        );
    }

    /**
     * Creates common headers for server responses.
     *
     * @return A map of headers typically used in HTTP server responses.
     */
    public static Map<CharSequence, CharSequence> regularResponseHeaders() {
        return Map.of(
                HttpHeaderNames.SERVER, IDENTIFY
        );
    }

    /**
     * Sets the Content-Length header in the provided headers based on the body size.
     *
     * @param headers The headers to modify.
     * @param body    The body whose size is used to set the Content-Length.
     */
    public static void setContentLength(HttpHeaders headers, Object body) {
        if (body instanceof ByteReader byteReader) {
            headers.add(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(byteReader.readableBytes()));
        } else if (body instanceof byte[] bytes) {
            headers.add(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(bytes.length));
        }
    }

    /**
     * Sets the Content-Type header based on the provided URL parameters or existing header.
     *
     * @param headers The headers to modify.
     * @param url     The URL used to determine the content type.
     */
    public static void setContentType(HttpHeaders headers, URL url) {
        CharSequence contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
        MediaType mediaType;
        if (StringUtil.isBlank(contentType)) {
            mediaType = MediaType.fromSerialization(url.getParam(KeyConstant.SERIALIZATION));
        } else {
            mediaType = MediaType.fromName(contentType);
        }
        if (mediaType != null) {
            headers.add(HttpHeaderNames.CONTENT_TYPE, mediaType.getName());
        }
    }

    /**
     * Encodes the body of the HTTP envelope into a byte array based on its content type.
     *
     * @param envelope The HTTP envelope containing the body to encode.
     * @return The encoded byte array of the body.
     */
    public static byte[] encodeBody(HttpEnvelope<?> envelope) throws IOException {
        CharSequence charSequence = envelope.headers().get(CONTENT_TYPE);
        return encodeBody(charSequence, envelope.body());
    }

    /**
     * Decodes the body of the HTTP request into the specified type.
     *
     * @param request  The HTTP request containing the body to decode.
     * @param bodyType The type to which the body should be decoded.
     * @return The decoded body as an object of the specified type.
     */
    public static Object decodeBody(HttpRequest<ByteReader> request, Type bodyType) throws IOException {
        ByteReader reader = request.body();
        byte[] bodyBytes = reader.readRemainingBytes();
        Serializer serializer = getSerializer(request.headers().get(CONTENT_TYPE));
        return serializer.deserialize(bodyBytes, bodyType);

    }

    /**
     * Decodes the body of the HTTP response into the specified type.
     *
     * @param response The HTTP response containing the body to decode.
     * @param bodyType The type to which the body should be decoded.
     * @return The decoded body as an object of the specified type, or the body as a string if the status code is not 200.
     */
    public static Object decodeBody(HttpResponse<ByteReader> response, Type bodyType) throws IOException {
        ByteReader reader = response.body();
        byte[] bodyBytes = reader.readRemainingBytes();
        if (response.statusCode() == 200) {
            Serializer serializer = getSerializer(response.headers().get(CONTENT_TYPE));
            return serializer.deserialize(bodyBytes, bodyType);
        } else {
            return new String(bodyBytes);
        }
    }

    /**
     * Converts the given url protocol to a standard scheme.
     *
     * @param url the URL to convert
     * @return the standard scheme as a String
     * @throws UnsupportedOperationException if the protocol is unsupported
     */
    public static String toStandardScheme(URL url) {
        return switch (url.protocol()) {
            case HTTP, H2C -> HTTP;
            case HTTPS, H2 -> HTTPS;
            default -> throw new UnsupportedOperationException("Unsupported protocol: " + url.protocol());
        };
    }

    /**
     * Acquires the appropriate HTTP version based on the given url protocol.
     *
     * @param url the URL to evaluate
     * @return the corresponding HttpVersion
     * @throws UnsupportedOperationException if the protocol is unsupported
     */
    public static HttpVersion acquireHttpVersion(URL url) {
        return switch (url.protocol()) {
            case HTTP, HTTPS -> HttpVersion.HTTP_1_1;
            case H2C, H2 -> HttpVersion.HTTP_2_0;
            default -> throw new UnsupportedOperationException("Unsupported protocol: " + url.protocol());
        };
    }

    /**
     * Encodes the body into a byte array based on its content type.
     *
     * @param contentType The content type used to determine the serialization method.
     * @param body        The body to encode.
     * @return The encoded byte array of the body.
     */
    private static byte[] encodeBody(CharSequence contentType, Object body) throws IOException {
        if (body == null) {
            return new byte[0];
        }
        return getSerializer(contentType).serialize(body);
    }

    /**
     * Retrieves the appropriate serializer based on the given content type.
     *
     * @param contentType The content type used to load the serializer.
     * @return The serializer corresponding to the content type.
     * @throws UnsupportedOperationException if the content type is not supported.
     */
    private static Serializer getSerializer(CharSequence contentType) {
        MediaType mediaType = MediaType.fromName(contentType);
        if (mediaType == null) {
            throw new UnsupportedOperationException("Unsupported content type: " + contentType + "'s serialization");
        }
        return ExtensionLoader.loadExtension(Serializer.class, mediaType.getSerialization());
    }

}

