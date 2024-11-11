package io.effi.rpc.core.caller;

import io.effi.rpc.core.stream.StreamObserver;

public interface CallStreamObserver extends StreamObserver<Object[]> {

    @Override
    void onNext(Object... data);

}
