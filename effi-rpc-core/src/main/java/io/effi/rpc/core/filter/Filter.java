package io.effi.rpc.core.filter;

import io.effi.rpc.core.Invocation;
import io.effi.rpc.common.extension.Ordered;
import io.effi.rpc.core.result.Result;

/**
 * Filter that can intercept RPC calls.
 *
 * Filters are used to modify or enhance the behavior of RPC calls.
 * They can perform pre-processing and post-processing tasks around
 * the invocation of remote methods. The filters are executed in a
 * defined order, allowing for flexible processing of the RPC flow.
 *
 */
@FunctionalInterface
public interface Filter extends Ordered {

    /**
     * Executes the filter logic.
     *
     * This method should implement the filter's functionality.
     * Always invoke {@link Invocation#invoke()} to proceed with the
     * next filter in the chain or to execute the actual remote method.
     *
     * @param invocation the invocation context containing parameters
     *                   and metadata for the RPC call.
     * @return the result of the invocation, which can include the
     *         response from the remote method or any modifications
     *         made by this filter.
     */
    Result doFilter(Invocation<Result> invocation);
}


