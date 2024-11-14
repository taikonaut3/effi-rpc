package io.effi.rpc.protocol;

import io.effi.rpc.common.constant.Platform;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Caller;
import io.effi.rpc.core.Invocation;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.core.filter.FilterChain;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.core.support.InvocationSupport;
import io.effi.rpc.metrics.MetricsSupport;
import io.effi.rpc.protocol.support.caller.AbstractCaller;
import io.effi.rpc.transport.Protocol;

import java.net.InetSocketAddress;

/**
 * @Author WenBo Zhou
 * @Date 2024/11/12 13:32
 */
public class RpcSupport {

    public static CallInvocation<?> locateCall(Caller<?> caller, ReplyFuture future, Object[] args) {
        Protocol protocol = protocol(caller);
        CallInvocation<?> invocation = protocol.createInvocation(future, caller, args);
        FilterChain filterChain = caller.filterChain();
        MetricsSupport.recordStartTime(invocation);
        if (Platform.isJvmShuttingDown()) {
            throw new RpcException("JVM is shutting down");
        }
        Invocation<Result> rpcInvocation = invocation.rebase(() -> {
            var filterInvocation = invocation.rebase(() -> {
                InetSocketAddress remoteAddress = caller.locator().locate(invocation);
                InvocationSupport.updateAddress(invocation, remoteAddress);
                Invocation<Result> chosenInvocation = invocation.rebase(() -> {
                    invocation.future().whenComplete(result -> {
                        Invocation<Result> resultInvocation = invocation.rebase(() -> result);
                        filterChain.execute(resultInvocation, caller.replyFilters());
                    });
                    return new Result(invocation.requestUrl(), invocation.future());
                });
                return filterChain.execute(chosenInvocation, caller.chosenFilters());
            });
            return filterChain.execute(filterInvocation, caller.filters());
        });
        rpcInvocation.invoke();
        return invocation;
    }

    private static Protocol protocol(Caller<?> caller) {
        if (caller instanceof AbstractCaller<?> abstractCaller) {
            return abstractCaller.protocol();
        }
        return ExtensionLoader.loadExtension(Protocol.class, caller.url().protocol());
    }

}
