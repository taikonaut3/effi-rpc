package io.effi.rpc.transport.http;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.common.url.URL;

/**
 * Http method.
 */
public enum HttpMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static final AttributeKey<HttpMethod> ATTRIBUTE_KEY = AttributeKey.valueOf(KeyConstant.HTTP_METHOD);

    public static HttpMethod getOf(URL url) {
        return url.get(HttpMethod.ATTRIBUTE_KEY);
    }

}
