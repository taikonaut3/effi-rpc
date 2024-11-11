package io.effi.rpc.protocol.support;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.filter.FilterChain;
import io.effi.rpc.core.result.Result;

import java.util.List;

/**
 * Default implementation of the {@link FilterChain} interface.
 * <p>
 * The {@link DefaultFilterChain} manages the execution of a sequence of
 * filters during an RPC call. It iterates through the provided list of
 * filters, invoking each filter in order and allowing for modifications
 * to the invocation before proceeding to the next filter.
 */
@Extension(Component.DEFAULT)
public class DefaultFilterChain implements FilterChain {

    @Override
    public Result execute(Invocation<Result> invocation, List<? extends Filter> filters) {
        FilterInvocation<Result> filterInvocation = new FilterInvocation<>(invocation);
        return doFilter(invocation, filterInvocation, filters, 0);
    }

    private Result doFilter(Invocation<Result> invocation, Invocation<Result> filterInvocation, List<? extends Filter> filters, int index) {
        if (filters == null || index == filters.size()) {
            return invocation.invoke();
        }
        Filter filter = filters.get(index);
        return filter.doFilter(filterInvocation.rebase(() -> doFilter(invocation, filterInvocation, filters, index + 1)));
    }

    private static class FilterInvocation<T> extends AbstractInvocation<T> {
        FilterInvocation(Invocation<T> invocation) {
            super(invocation.portal(), invocation.requestUrl(), invocation.invoker(), invocation.args());
        }
    }
}