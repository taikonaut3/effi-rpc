package io.effi.rpc.common.url;

import java.lang.annotation.*;

/**
 * Mark field that should be included in the parameterization process.
 * It allows specifying a key that will be used when mapping the field's value to a parameter.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parameter {

    /**
     * The key that represents the annotated field during parameterization.
     *
     * @return the key as a String
     */
    String value();
}

