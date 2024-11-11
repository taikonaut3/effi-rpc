package io.effi.rpc.core;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.Attributes;
import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.url.QueryPath;
import io.effi.rpc.common.url.URLSource;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.filter.FilterChain;
import io.effi.rpc.core.filter.ReplyFilter;
import io.effi.rpc.core.manager.Manager;

import java.util.List;

/**
 * Wrapper for client and server invocation.
 * <p>
 * This interface provides a unified mechanism for invoking methods on both
 * client and server sides. For the client, it allows direct initiation of remote
 * calls through the {@link #invoke(Object...)} method. For the server, it
 * facilitates invoking the methods of the corresponding service, allowing for
 * seamless interaction across distributed components.
 *
 * @param <R> The return type of the invoked
 */
public interface Invoker<R> extends URLSource, Attributes, Manager.Key {

    /**
     * Returns the path that is used during the invocation.
     *
     * @return the {@link QueryPath} associated with the invocation.
     */
    QueryPath queryPath();

    /**
     * Invokes the method with the provided arguments.
     *
     * <p>This method serves as the entry point for both local and remote invocation.</p>
     *
     * @param args the arguments to be passed to the invoked method.
     * @return the result of the invocation, which can be of type {@code R}
     * @throws RpcException if an error occurs during the invocation.
     */
    R invoke(Object... args) throws RpcException;

    /**
     * Returns the TypeToken representing the return type.
     *
     * @return the TypeToken of the return type, which can be used to infer
     * the actual type at runtime.
     */
    TypeToken<?> returnType();

    /**
     * Adds one or more filters to the current invoker.
     *
     * @param filters an array of {@link Filter} objects to be added.
     *                Each filter can modify or process the incoming
     *                requests and outgoing responses.
     */
    void addFilter(Filter... filters);

    /**
     * Returns a list of filters that are applied to the invocation process.
     *
     * @return a list of {@link Filter} instances to be applied during the invocation.
     */
    List<Filter> filters();

    /**
     * Returns a list of reply filters that are applied to the response of the invocation.
     *
     * @return a list of {@link ReplyFilter} instances to be applied to the invocation's response.
     */
    List<ReplyFilter> replyFilters();

    /**
     * Returns the filter chain that will be used for invocation processing.
     *
     * @return the {@link FilterChain} instance to be used during invocation.
     */
    FilterChain filterChain();

    @Override
    default String managerKey() {
        return url().uri();
    }
}


