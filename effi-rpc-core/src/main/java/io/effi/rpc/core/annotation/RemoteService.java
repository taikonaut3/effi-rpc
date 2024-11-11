package io.effi.rpc.core.annotation;

import io.effi.rpc.common.constant.Component;

import java.lang.annotation.*;

/**
 * Indicates a Remote service.
 * <p>Methods of the class with {@link RemoteService} can be called by the client.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RemoteService {

    /**
     * The remoteService name.
     */
    String value();

    /**
     * The proxy type.
     */
    String proxy() default Component.ProxyFactory.CGLIB;

}
