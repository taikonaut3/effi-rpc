package io.effi.rpc.core.annotation;

import io.effi.rpc.common.constant.Constant;

import java.lang.annotation.*;

/**
 * Indicates a remote caller.
 * <p>Used on interface, remoteCaller can create a proxy for that interface to perform remote invocations.<p/>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoteCaller {

    /**
     * The remoteApplication name or URL.
     */
    String value();

    /**
     * The proxy type.
     */
    String proxy() default Constant.DEFAULT_PROXY;

    /**
     * Is it only when the first protocol call is made that the registration center is actually connected to Get the available services.
     * <p>The default gets available services when {@link Caller} is created.</p>
     */
    boolean lazyDiscover() default false;

    /**
     * If Rpc call failed, then reflect fallBacker.
     *
     * @return
     */
    Class<?> fallback() default Void.class;

}
