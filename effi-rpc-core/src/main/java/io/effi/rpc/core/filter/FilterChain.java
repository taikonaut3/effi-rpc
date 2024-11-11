package io.effi.rpc.core.filter;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.result.Result;

import java.util.List;

/**
 * Chain of filters used to process an RPC invocation.
 * The filter chain manages the execution of multiple filters in a specified order,
 * allowing for cross-cutting concerns like logging, authentication, and validation
 * to be applied seamlessly during the invocation process.
 *
 * Filters can modify the invocation or its result, as well as determine whether to
 * proceed to the next filter in the chain.
 */
@Extensible(Component.DEFAULT)
public interface FilterChain {

    /**
     * Executes the filter chain.
     *
     * This method takes an {@link Invocation} and a list of filters,
     * applying each filter in sequence. Each filter can process the
     * invocation and optionally call the next filter in the chain.
     *
     * @param invocation The invocation context for the RPC call.
     * @param filters    The list of filters to be invoked in order.
     * @return The result of the invocation after processing through the filters.
     */
    Result execute(Invocation<Result> invocation, List<? extends Filter> filters);

}


