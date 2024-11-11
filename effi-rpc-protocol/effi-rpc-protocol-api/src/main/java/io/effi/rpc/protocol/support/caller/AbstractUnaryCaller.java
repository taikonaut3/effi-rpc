package io.effi.rpc.protocol.support.caller;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.protocol.support.CompletableReplyFuture;
import io.effi.rpc.core.caller.UnaryCaller;
import io.effi.rpc.protocol.support.builder.CallerBuilder;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractUnaryCaller<R> extends AbstractCaller<CompletableFuture<R>> implements UnaryCaller<R> {
    protected AbstractUnaryCaller(URL url, CallerBuilder<?, ?> builder) {
        super(url, builder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<R> call(Object... args) throws RpcException {
        CompletableReplyFuture future = new CompletableReplyFuture();
        CallInvocation<?> invocation = protocol.createInvocation(future, this, args);
        call(invocation);
        return (CompletableFuture<R>) future.completableFuture();
    }

}
