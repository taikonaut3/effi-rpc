package io.effi.rpc.core.annotation;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.core.filter.Filter;

import java.lang.annotation.*;

/**
 * Common config for the client and the server.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Config {

    /**
     * Serialization type.
     */
    String serialization() default Constant.DEFAULT_SERIALIZATION;

    /**
     * Compression type.
     */
    String compression() default Constant.DEFAULT_COMPRESSION;

    /**
     * The filter chain for {@link Invoker}.
     */
    String filterChain() default Component.DEFAULT;

    /**
     * Invoke Filters.
     *
     * @see Filter
     */
    String[] filters() default {};
}

