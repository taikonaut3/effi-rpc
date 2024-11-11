package io.effi.rpc.common.extension.spi;

import io.effi.rpc.common.url.URL;

import java.lang.annotation.*;

/**
 * Indicate that the annotated interface is a Service Provider Interface (SPI).
 * This annotation should be applied to interfaces that will have multiple implementations
 * managed by the extension loader.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extensible {

    /**
     * Specifies the default implementation class name.
     *
     * @return the name of the default implementation.
     * @see Extension#value()
     */
    String value() default "";

    /**
     * When using {@link ExtensionLoader#loadExtension(Class, URL)},
     * Can select the extension by using the key configured in the params of the url.
     *
     * @return the extension key.
     */
    String key() default "";

    /**
     * Indicates whether lazy loading is enabled for the extension.
     * This differentiates it from the standard JDK SPI (Service Provider Interface).
     *
     * @return true if lazy loading is enabled; false otherwise.
     */
    boolean lazyLoad() default true;

}
