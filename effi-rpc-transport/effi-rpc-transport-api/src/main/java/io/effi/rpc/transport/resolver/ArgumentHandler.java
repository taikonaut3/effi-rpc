package io.effi.rpc.transport.resolver;

import io.effi.rpc.core.Callee;
import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.transport.Envelope;

/**
 * Hande argument extraction from requests on the server side.
 *
 * @param <REQ> the type of the request being handled
 */
public interface ArgumentHandler<REQ extends Envelope> {

    /**
     * Determines if the given argument type is supported for the specified callee.
     *
     * @param callee the callee object representing the method being invoked
     * @param mapper the parameter mapper used for mapping request parameters to method parameters
     * @return true if the argument is supported; false otherwise
     */
    boolean isSupportedArgument(Callee<?> callee, ParameterMapper mapper);

    /**
     * Handles the extraction of the required parameters from the request.
     *
     * @param request the incoming request from which parameters are to be extracted
     * @param callee the callee object representing the method being invoked
     * @param mapper the parameter mapper used to extract parameters from the request
     * @return the extracted parameter value, or null if no value could be extracted
     */
    Object handler(REQ request, Callee<?> callee, ParameterMapper mapper);
}

