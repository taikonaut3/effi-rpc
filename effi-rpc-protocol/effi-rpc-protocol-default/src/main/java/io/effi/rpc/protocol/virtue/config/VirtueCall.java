package io.effi.rpc.protocol.virtue.config;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.core.annotation.Protocol;
import io.effi.rpc.core.annotation.RemoteService;

import java.lang.annotation.*;

/**
 * Use Virtue protocol to make RPC call.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Protocol(Component.Protocol.VIRTUE)
public @interface VirtueCall {

    /**
     * Remote Service Name.
     *
     * @return
     * @see RemoteService#value()
     */
    String service();

    /**
     * Remote Method.
     *
     * @return
     * @see VirtueCallable#name()
     */
    String callMethod() default "";

}
