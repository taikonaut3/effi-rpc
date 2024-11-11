package io.effi.rpc.core.caller;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.core.stream.StreamObserver;

public interface ServerStreamCaller<R> extends Caller<StreamObserver<R>> {

    /**
     * Response streaming.
     *
     * @param responseObserver
     * @param args
     * @throws RpcException
     */
    void call(StreamObserver<R> responseObserver, Object... args);

}
