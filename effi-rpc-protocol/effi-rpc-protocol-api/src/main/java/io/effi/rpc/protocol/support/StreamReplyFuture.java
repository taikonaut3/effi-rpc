package io.effi.rpc.protocol.support;

import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.core.stream.StreamObserver;

import java.util.function.Consumer;

public class StreamReplyFuture extends ReplyFuture {

    private final StreamObserver<Object> streamObserver;

    public StreamReplyFuture(StreamObserver<Object> streamObserver) {
        this.streamObserver = AssertUtil.notNull(streamObserver, "streamObserver cannot be null");
    }

    @Override
    public void whenComplete(Consumer<Result> consumer) {
        completedConsumers.add(consumer);
    }

    @Override
    public void doComplete(Object value) {
        streamObserver.onNext(value);
    }

    @Override
    protected void doCompleteExceptionally(Throwable t) {
        streamObserver.onError(t);
    }

}
