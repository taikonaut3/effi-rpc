package io.effi.rpc.protocol.support.caller;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.caller.ServerStreamCaller;
import io.effi.rpc.protocol.support.StreamReplyFuture;
import io.effi.rpc.core.stream.StreamObserver;
import io.effi.rpc.protocol.support.builder.CallerBuilder;

public abstract class AbstractServerStreamCaller<R> extends AbstractCaller<StreamObserver<R>>
        implements ServerStreamCaller<R> {
    protected AbstractServerStreamCaller(URL url, CallerBuilder<?, ?> builder) {
        super(url, builder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void call(StreamObserver<R> responseObserver, Object... args) {
        StreamReplyFuture future = new StreamReplyFuture((StreamObserver<Object>) responseObserver);
        CallInvocation<?> invocation = protocol.createInvocation(future, this, args);
        call(invocation);
    }
}
