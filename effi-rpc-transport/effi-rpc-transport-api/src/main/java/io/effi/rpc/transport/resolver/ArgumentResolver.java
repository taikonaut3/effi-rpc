package io.effi.rpc.transport.resolver;

import io.effi.rpc.core.arg.ParameterMapper;
import io.effi.rpc.core.Callee;
import io.effi.rpc.transport.Envelope;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolve arguments from a request to the parameters needed by a callee.
 *
 * @param <REQ> the type of the request which extends the Envelope interface
 */
public abstract class ArgumentResolver<REQ extends Envelope> {

    // List to hold registered argument handlers for processing the request parameters
    private final List<ArgumentHandler<REQ>> handlers = new ArrayList<>();

    /**
     * Constructor that initializes the argument resolver and calls the initialize method
     * for subclasses to set up any specific configurations or handlers.
     */
    protected ArgumentResolver() {
        initialize();
    }

    /**
     * Resolves the parameters for the given request based on the specified callee.
     * It iterates through the parameter mappers and tries to use registered argument handlers
     * to extract the corresponding values from the request.
     *
     * @param request the incoming request from which parameters are extracted
     * @param callee  the callee object representing the method to be invoked
     * @return an array of resolved arguments corresponding to the parameter mappers
     */
    public Object[] resolve(REQ request, Callee<?> callee) {
        // Get parameter mappers from the callee
        ParameterMapper[] parameterMappers = callee.parameterMappers();
        Object[] args = new Object[parameterMappers.length];
        for (int i = 0; i < parameterMappers.length; i++) {
            for (ArgumentHandler<REQ> handler : handlers) {
                ParameterMapper mapper = parameterMappers[i];
                if (handler.isSupportedArgument(callee, mapper)) {
                    // Attempt to extract the argument using the handler
                    Object result = handler.handler(request, callee, mapper);
                    if (result != null) {
                        args[i] = result;
                        // Move to the next parameter mapper
                        break;
                    }
                }
            }
        }
        return args;
    }

    /**
     * Adds a new argument handler to the list of handlers.
     *
     * @param handler the argument handler to be added
     * @return the current instance of ArgumentResolver for method chaining
     */
    public ArgumentResolver<REQ> add(ArgumentHandler<REQ> handler) {
        handlers.add(handler);
        return this; // Return the current instance for chaining
    }

    /**
     * Abstract method for initializing the argument resolver. Subclasses must provide
     * an implementation to set up specific configurations or register handlers.
     */
    protected abstract void initialize();
}

