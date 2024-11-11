package io.effi.rpc.transport.resolver;

import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.transport.Envelope;

import java.io.IOException;

/**
 * Handling the extraction and processing of response values.
 *
 * @param <RESP> the type of the response envelope that this resolver handles,
 *               which must extend the {@link Envelope} interface
 */
public interface ReplyResolver<RESP extends Envelope> {

    /**
     * Resolves the specified response into an appropriate object.
     * <p>
     * It may involve deserialization operations
     * and modifications to the response body. The resolved data
     * is returned in a format suitable for further processing or
     * for returning to the caller.
     * </p>
     *
     * @param response the response envelope containing the data to be resolved
     * @param caller   the caller object representing the entity that
     *                 initiated the request and expects a response
     * @return an object representing the resolved response data, which may
     * vary based on the implementation and the response type
     */
    Result resolve(RESP response, Caller<?> caller) throws IOException;
}

