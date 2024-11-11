package io.effi.rpc.protocol.support.caller;

import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.arg.Body;
import io.effi.rpc.core.caller.Caller;
import io.effi.rpc.core.stream.StreamObserver;
import io.effi.rpc.protocol.support.StreamReplyFuture;
import io.effi.rpc.transport.Protocol;

public class DefaultCallStreamObserver<T> implements StreamObserver<T> {

    private final StreamReplyFuture future;

    private final Caller<?> caller;

    private final Protocol protocol;

    public DefaultCallStreamObserver(StreamReplyFuture future, Caller<?> caller) {
        this.future = future;
        this.caller = caller;
        this.protocol = ExtensionLoader.loadExtension(Protocol.class, caller.url().protocol());
    }

    @Override
    public void onNext(T data) {
        CallInvocation<?> invocation = protocol.createInvocation(future, caller, new Object[]{Body.wrap(data)});
        caller.call(invocation);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
