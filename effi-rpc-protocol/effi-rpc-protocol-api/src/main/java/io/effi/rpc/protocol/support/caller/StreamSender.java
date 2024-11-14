package io.effi.rpc.protocol.support.caller;

import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.stream.StreamObserver;
import io.effi.rpc.protocol.support.StreamReplyFuture;
import io.effi.rpc.transport.channel.Channel;

public class StreamSender<T> implements StreamObserver<T> {

    private final StreamReplyFuture future;

    private final CallInvocation<?> invocation;

    private Channel channel;

    public StreamSender(Channel channel, CallInvocation<?> invocation) {
        this.channel = channel;
        this.invocation = invocation;
        this.future = (StreamReplyFuture) invocation.future();
    }

    @Override
    public void onNext(T data) {
        channel.send(data);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
