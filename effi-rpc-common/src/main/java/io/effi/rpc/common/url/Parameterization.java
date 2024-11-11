package io.effi.rpc.common.url;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.util.ReflectionUtil;
import io.effi.rpc.common.util.StringUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide a mechanism to collect parameters from fields annotated with {@link Parameter}.
 * Implementing classes can call the `parameterization` method to retrieve a map of parameters.
 */
public interface Parameterization {

    /**
     * Collects parameters from fields annotated with {@link Parameter} and returns them as a map.
     *
     * @return a Map containing parameter keys and their corresponding values
     */
    default Map<String, String> parameterization() {
        HashMap<String, String> map = new LinkedHashMap<>();
        List<Field> fields = ReflectionUtil.getAllFields(this.getClass());

        // Iterate through each field to check for the Parameter annotation.
        for (Field field : fields) {
            if (field.isAnnotationPresent(Parameter.class)) {
                String key = field.getAnnotation(Parameter.class).value();
                String value;
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(this);
                    if (fieldValue != null) {
                        value = String.valueOf(fieldValue);
                        if (!StringUtil.isBlank(value)) {
                            map.put(key, value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw RpcException.wrap(e);
                }
            }
        }
        return map;
    }
}

