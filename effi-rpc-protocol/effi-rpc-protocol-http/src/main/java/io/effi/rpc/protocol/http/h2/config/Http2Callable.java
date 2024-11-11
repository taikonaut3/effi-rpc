package io.effi.rpc.protocol.http.h2.config;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.core.annotation.Protocol;
import io.effi.rpc.transport.http.HttpMethod;

import java.lang.annotation.*;

/**
 * Support be h2 protocol call.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Protocol(Component.Protocol.H2)
public @interface Http2Callable {

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
