package io.effi.rpc.protocol.virtue.config;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.core.annotation.Protocol;

import java.lang.annotation.*;

/**
 * Support be Virtue protocol call.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Protocol(Component.Protocol.VIRTUE)
public @interface VirtueCallable {

    /**
     * name of the method.
     *
     * @return
     */
    String name() default "";

    /**
     * desc of the method.
     *
     * @return
     */
    String desc() default "";

}
