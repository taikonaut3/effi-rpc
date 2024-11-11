package io.effi.rpc.common.extension.spi;

import java.lang.annotation.*;

/**
 * Mark the implementation class of an SPI interface, indicating that this class
 * is available for SPI-based selection. This annotation should be used on classes
 * that serve as implementations of SPI interfaces.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extension {

    /**
     * Specifies the name of the current implementation.
     *
     * @return the name of the implementation.
     */
    String[] value() default {};

    /**
     * Specifies the interfaces for which this class provides an implementation.
     *
     * @return the interfaces provided by this class. If not set, applies to all interfaces
     *         marked with {@link Extensible}.
     */
    Class<?>[] interfaces() default {};

    /**
     * Indicates whether this extension should override other extensions with the same name.
     *
     * @return true if this extension should override; false otherwise.
     */
    boolean override() default false;

    /**
     * Specifies the scope of the extension, defaulting to {@link Scope#SINGLETON}.
     *
     * @return the scope of the extension.
     */
    Scope scope() default Scope.SINGLETON;
}
