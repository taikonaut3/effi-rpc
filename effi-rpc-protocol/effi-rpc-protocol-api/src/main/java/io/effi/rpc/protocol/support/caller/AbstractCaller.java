package io.effi.rpc.protocol.support.caller;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.constant.Platform;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.Ordered;
import io.effi.rpc.common.extension.collection.LazyList;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.common.util.CollectionUtil;
import io.effi.rpc.core.*;
import io.effi.rpc.core.config.ClientConfig;
import io.effi.rpc.core.filter.ChosenFilter;
import io.effi.rpc.core.filter.Filter;
import io.effi.rpc.core.filter.ReplyFilter;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.core.support.InvocationSupport;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.metrics.CallerMetrics;
import io.effi.rpc.metrics.MetricsSupport;
import io.effi.rpc.protocol.RpcSupport;
import io.effi.rpc.protocol.support.AbstractInvoker;
import io.effi.rpc.protocol.support.CompletableReplyFuture;
import io.effi.rpc.protocol.support.builder.CallerBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract implementation of {@link Caller}.
 *
 * @param <R>
 */
@Getter
@Accessors(fluent = true)
public abstract class AbstractCaller<R> extends AbstractInvoker<CompletableFuture<R>> implements Caller<R> {

    protected final Logger logger = LoggerFactory.getLogger(AbstractCaller.class);

    protected Portal portal;

    protected ClientConfig clientConfig;

    protected Locator locator;

    protected List<ChosenFilter> chosenFilters = new LazyList<>(ArrayList::new);

    protected AbstractCaller(URL url, CallerBuilder<?, ?> builder) {
        super(url, builder);
        this.portal = AssertUtil.notNull(builder.portal(), "portal cannot be null");
        this.clientConfig = AssertUtil.notNull(builder.clientConfig(), "client config cannot be null");
        this.locator = AssertUtil.notNull(builder.locator(), "locator cannot be null");
        this.returnType = builder.returnType();
        this.portal.register(this);
        orderFilters(this.portal);
        set(KeyConstant.LAST_CALL_INDEX, new AtomicInteger(-1));
        set(CallerMetrics.ATTRIBUTE_KEY, new CallerMetrics());
    }

    @Override
    public void addFilter(Filter... filters) {
        if (CollectionUtil.isNotEmpty(filters)) {
            for (Filter filter : filters) {
                if (filter instanceof ChosenFilter chosenFilter) {
                    CollectionUtil.addUnique(chosenFilters, chosenFilter);
                } else if (filter instanceof ReplyFilter replyFilter) {
                    CollectionUtil.addUnique(replyFilters, replyFilter);
                } else {
                    CollectionUtil.addUnique(this.filters, filter);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<R> call(Object... args) throws RpcException {
        CompletableReplyFuture future = new CompletableReplyFuture();
        CallInvocation<?> invocation = RpcSupport.locateCall(this, future, args);
        protocol.sendRequest(invocation);
        return (CompletableFuture<R>) future.completableFuture();
    }

    @Override
    public CompletableFuture<R> invoke(Object... args) throws RpcException {
        return call(args);
    }

    @Override
    public void call(CallInvocation<?> invocation) {
        MetricsSupport.recordStartTime(invocation);
        if (Platform.isJvmShuttingDown()) {
            throw new RpcException("JVM is shutting down");
        }
        Invocation<Result> filterInvocation = invocation.rebase(() -> {
            var chosenInvocation = invocation.rebase(() -> invokeChosenFilter(invocation));
            return filterChain.execute(chosenInvocation, filters);
        });
        filterInvocation.invoke();
    }

    protected Result invokeChosenFilter(CallInvocation<?> invocation) {
        InetSocketAddress remoteAddress = locator.locate(invocation);
        InvocationSupport.updateAddress(invocation, remoteAddress);
        Invocation<Result> chosenInvocation = invocation.rebase(() -> sendRequest(invocation));
        return filterChain.execute(chosenInvocation, chosenFilters);
    }

    protected Result sendRequest(CallInvocation<?> invocation) {
        URL url = invocation.callerUrl();
        protocol.sendRequest(invocation);
        invocation.future().whenComplete(result -> {
            Invocation<Result> resultInvocation = invocation.rebase(() -> result);
            filterChain.execute(resultInvocation, replyFilters);
        });
        return new Result(url, invocation.future());
    }

    protected void orderFilters(Portal portal) {
        List<Filter> sharedFilter = portal.filterManager().sharedValues();
        addFilter(sharedFilter.toArray(Filter[]::new));
        filters = Ordered.order(filters);
        chosenFilters = Ordered.order(chosenFilters);
        replyFilters = Ordered.order(replyFilters);
    }

}
