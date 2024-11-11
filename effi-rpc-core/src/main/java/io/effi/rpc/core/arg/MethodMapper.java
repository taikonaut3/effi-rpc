package io.effi.rpc.core.arg;

import io.effi.rpc.core.RemoteService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;


/**
 * Mapping methods to remote service calls.
 *
 * @param <T> the type of the remote service
 */
@Accessors(fluent = true)
@Getter
@AllArgsConstructor
public abstract class MethodMapper<T> {

    /**
     * The remote service associated with this method mapper.
     */
    protected RemoteService<T> remoteService;

    /**
     * The method to be mapped.
     */
    protected Method method;

    /**
     * An array of parameter mappers for the method parameters.
     */
    protected ParameterMapper[] parameterMappers;

}
