package io.effi.rpc.core.stream;

@FunctionalInterface
public interface StreamFunction<R, T> {

    void apply(StreamObserver<R> requestObserver, T data);
}
