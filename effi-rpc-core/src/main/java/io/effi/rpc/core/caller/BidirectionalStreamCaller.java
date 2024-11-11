package io.effi.rpc.core.caller;

import io.effi.rpc.core.stream.StreamObserver;

public interface BidirectionalStreamCaller<R> extends Caller<StreamObserver<R>> {

    <T> StreamObserver<T> call(StreamObserver<R> responseObserver);

}
