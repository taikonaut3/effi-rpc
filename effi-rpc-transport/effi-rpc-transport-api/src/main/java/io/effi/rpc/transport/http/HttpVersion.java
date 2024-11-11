package io.effi.rpc.transport.http;

import io.effi.rpc.common.extension.AttributeKey;

/**
 * Http version.
 */
public enum HttpVersion {

    HTTP_1_0, HTTP_1_1, HTTP_2_0;

    public static final AttributeKey<HttpVersion> ATTRIBUTE_KEY = AttributeKey.valueOf("httpVersion");
}
