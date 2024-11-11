package io.effi.rpc.protocol.support.caller;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.caller.BidirectionalStreamCaller;
import io.effi.rpc.core.stream.StreamObserver;
import io.effi.rpc.protocol.support.StreamReplyFuture;
import io.effi.rpc.protocol.support.builder.CallerBuilder;

public class AbstractBidirectionalStreamCaller<R> extends AbstractCaller<StreamObserver<R>>
        implements BidirectionalStreamCaller<R> {
    protected AbstractBidirectionalStreamCaller(URL url, CallerBuilder<?, ?> builder) {
        super(url, builder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> StreamObserver<T> call(StreamObserver<R> responseObserver) {
        StreamReplyFuture future = new StreamReplyFuture((StreamObserver<Object>) responseObserver);
        return new DefaultCallStreamObserver<>(future, this);
    }
}
