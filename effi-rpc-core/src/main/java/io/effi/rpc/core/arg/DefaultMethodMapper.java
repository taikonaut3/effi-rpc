package io.effi.rpc.core.arg;

import io.effi.rpc.common.extension.Builder;
import io.effi.rpc.core.RemoteService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link MethodMapper}.
 * Provides one-to-one parameter mapping for each field of the method.
 *
 * @param <T> the type of the remote service
 */
public class DefaultMethodMapper<T> extends MethodMapper<T> {

    DefaultMethodMapper(RemoteService<T> remoteService, Method method, ParameterMapper[] parameterMappers) {
        super(remoteService, method, parameterMappers);
    }

    /**
     * Creates a builder for DefaultMethodMapper.
     *
     * @param remoteService the remote service associated with the method
     * @param methodName the name of the method to map
     * @param <T> the type of the remote service
     * @return a builder for DefaultMethodMapper
     */
    public static <T> DefaultMethodMapperBuilder<T> builder(RemoteService<T> remoteService, String methodName) {
        return new DefaultMethodMapperBuilder<>(remoteService, methodName);
    }

    /**
     * Builder class for constructing DefaultMethodMapper instances.
     *
     * @param <T> the type of the remote service
     */
    public static class DefaultMethodMapperBuilder<T> implements Builder<DefaultMethodMapper<T>> {

        private final RemoteService<T> remoteService;
        private final String methodName;
        private final List<Mapping> argMapping = new ArrayList<>();

        DefaultMethodMapperBuilder(RemoteService<T> remoteService, String methodName) {
            this.remoteService = remoteService;
            this.methodName = methodName;
        }

        /**
         * Specifies the parameter type for mapping.
         *
         * @param parameterType the class of the parameter type
         * @return the current builder instance
         */
        public DefaultMethodMapperBuilder<T> parameterType(Class<?> parameterType) {
            return mappedParameterType(parameterType, null);
        }

        /**
         * Maps a parameter type to an Argument.
         *
         * @param argType the class of the argument type
         * @param mappedArg the argument to map to
         * @return the current builder instance
         */
        public DefaultMethodMapperBuilder<T> mappedParameterType(Class<?> argType, Argument mappedArg) {
            Mapping mapping = new Mapping(argType, mappedArg);
            argMapping.add(mapping);
            return this;
        }

        @Override
        public DefaultMethodMapper<T> build() {
            Method method = validMethod();
            Parameter[] parameters = method.getParameters();
            ParameterMapper[] wrappers = new ParameterMapper[parameters.length];
            for (int i = 0; i < argMapping.size(); i++) {
                wrappers[i] = new ParameterMapper(parameters[i], argMapping.get(i).mappedArg());
            }
            return new DefaultMethodMapper<>(remoteService, method, wrappers);
        }

        private Method validMethod() {
            Class<T> targetType = remoteService.targetType();
            Class<?>[] parameterTypes = argMapping.stream().map(Mapping::argType).toArray(Class[]::new);
            try {
                return targetType.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Can't find method: " + e.getMessage(), e);
            }
        }
    }

    private record Mapping(Class<?> argType, Argument mappedArg) {

    }

}

