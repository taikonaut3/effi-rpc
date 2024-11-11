package io.effi.rpc.protocol.http.annotation;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.core.annotation.Protocol;
import io.effi.rpc.transport.http.HttpMethod;

import java.lang.annotation.*;

/**
 * Support be http1.1 protocol call.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Protocol(Component.Protocol.HTTP)
public @interface HttpCallable {

    /**
     * The path of the HTTP request.
     */
    String path() default "";

    /**
     * The method of the HTTP request.
     */
    HttpMethod method() default HttpMethod.GET;

    /**
     * The headers of the HTTP request.
     */
    String[] headers() default {};

    /**
     * The content type of the HTTP.
     */
    String contentType() default "application/json";

    /**
     * Whether to use SSL.
     */
    boolean ssl() default true;
}
