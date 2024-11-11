package io.effi.rpc.core.arg;

import java.lang.reflect.Parameter;


/**
 * Mapping a method parameter to an associated method argument.
 *
 * @param parameter the method parameter
 * @param argument the argument to be used for the parameter
 */
public record ParameterMapper(Parameter parameter, Argument argument) {

}

