package io.effi.rpc.core;

import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.core.result.Result;

import java.lang.reflect.Method;

/**
 * Server callee for handling remote service calls.
 * <p>
 * Responsible for invoking methods on the remote
 * service and mapping parameters for the method invocation.
 *
 * @param <T> The type of the remote service interface.
 */
public interface Callee<T> extends Invoker<Object> {

    /**
     * Returns the remote service associated with this callee.
     *
     * @return the remote service instance.
     */
    RemoteService<T> remoteService();

    /**
     * Returns the method represented by this callee.
     *
     * @return the Method object representing the method to be invoked.
     */
    Method method();

    /**
     * Returns the parameter mappers for the method.
     *
     * @return an array of parameter mappers used for the method parameters.
     */
    ParameterMapper[] parameterMappers();

    /**
     * Returns an invocation and then invokes the associated method.
     *
     * @param invocation the invocation context containing parameters and metadata.
     * @return the result of the method invocation.
     */
    Result receive(ReceiveInvocation<?> invocation);

    /**
     * Returns the description of the server callee.
     *
     * @return a string describing the method and its purpose.
     */
    String desc();

    @Override
    default String managerKey() {
        return url().path();
    }
}

