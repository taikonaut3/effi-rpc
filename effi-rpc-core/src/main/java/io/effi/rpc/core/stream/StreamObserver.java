package io.effi.rpc.core.stream;

/**
 * Observer for streaming data, which handles the three possible states of a stream:
 * receiving data, encountering an error, and the stream being completed.
 *
 * @param <T> the type of the data being streamed
 */
public interface StreamObserver<T> {

    /**
     * Invoked when new data is available from the stream.
     *
     * @param data the data received in this event
     */
    void onNext(T data);

    /**
     * Invoked if an error occurs during streaming.
     *
     * @param throwable the error that caused the failure
     */
    void onError(Throwable throwable);

    /**
     * Invoked when the stream has completed successfully and no more data will be sent.
     */
    void onCompleted();
}
