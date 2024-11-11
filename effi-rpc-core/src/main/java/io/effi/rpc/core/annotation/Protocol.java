package io.effi.rpc.core.annotation;

import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.common.extension.spi.Extension;

import java.lang.annotation.*;

/**
 * Create the Invoker implementation of the extension protocol.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Protocol {

    /**
     * {@link io.effi.rpc.rpc.protocol.Protocol}‘s {@link Extensible#value()}.
     *
     * @return {@link Extension#value()}
     */
    String value();

}
