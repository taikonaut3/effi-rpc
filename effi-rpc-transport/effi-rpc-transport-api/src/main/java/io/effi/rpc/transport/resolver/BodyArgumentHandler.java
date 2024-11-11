package io.effi.rpc.transport.resolver;

import io.effi.rpc.core.Callee;
import io.effi.rpc.core.arg.Body;
import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.transport.Envelope;

/**
 * Handling body arguments in a request.
 *
 * @param <REQ> the type of the request being processed
 */
@FunctionalInterface
public interface BodyArgumentHandler<REQ extends Envelope> extends ArgumentHandler<REQ> {

    /**
     * Checks if the given parameter mapper represents a body argument.
     *
     * @param callee the callee object representing the method to be invoked
     * @param mapper the parameter mapper containing the argument details
     * @return {@link true} if the argument is a body argument; {@link false} otherwise
     */
    @Override
    default boolean isSupportedArgument(Callee<?> callee, ParameterMapper mapper) {
        return mapper.argument() instanceof Body<?>;
    }

    /**
     * Responsible for parsing the body from the request.
     * <p>
     * It may involve deserialization of the body content into the appropriate
     * object type as specified by the parameter mapper. After parsing, the
     * resulting object is set into the provided request instance.
     * </p>
     *
     * @param request the request object from which the body will be parsed
     * @param callee  the callee object representing the method to be invoked
     * @param mapper  the parameter mapper providing details on how to extract
     *                the body and its expected type
     * @return the parsed body object, or {@link null} if parsing fails or
     * if nobody is present in the request
     */
    @Override
    Object handler(REQ request, Callee<?> callee, ParameterMapper mapper);

}

