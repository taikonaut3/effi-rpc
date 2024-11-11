package io.effi.rpc.core.caller;

import io.effi.rpc.core.*;
import io.effi.rpc.core.config.ClientConfig;
import io.effi.rpc.core.filter.ChosenFilter;
import io.effi.rpc.core.result.Result;

import java.util.List;

/**
 * Client caller for making remote service calls.
 * <p>
 * Can initiate RPC calls through a Caller object. The call can be made either
 * synchronously or asynchronously, facilitating interaction with remote services.
 * This interface also provides methods for handling direct addresses and managing
 * options for the RPC call, allowing for a flexible and robust client-side implementation.
 *
 * @param <R> The return type of the remote service call.
 */
public interface Caller<R> extends Invoker<R>, PortalSource {

    /**
     * Returns the client configuration used for the RPC call.
     *
     * @return
     */
    ClientConfig clientConfig();

    /**
     * Returns the locator used for service discovery.
     *
     * @return
     */
    Locator locator();

    /**
     * Returns a list of chosen filters that will be applied to the RPC call.
     *
     * @return a list of {@link ChosenFilter} instances that are applied to the call.
     */
    List<ChosenFilter> chosenFilters();

    /**
     * Entry point for making RPC client calls.
     *
     * @param invocation the {@link Invocation} containing parameters for the call.
     * @return the result of the remote call encapsulated in a {@link Result} object.
     */
    void call(CallInvocation<?> invocation);

}



