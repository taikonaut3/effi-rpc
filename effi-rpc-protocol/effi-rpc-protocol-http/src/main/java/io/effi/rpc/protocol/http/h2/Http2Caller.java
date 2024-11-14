package io.effi.rpc.protocol.http.h2;

import io.effi.rpc.common.extension.TypeToken;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Caller;
import io.effi.rpc.core.stream.StreamObserver;
import io.effi.rpc.protocol.RpcSupport;
import io.effi.rpc.protocol.http.HttpCaller;
import io.effi.rpc.protocol.support.StreamReplyFuture;
import io.effi.rpc.protocol.support.caller.StreamSender;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.client.Client;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Http2 protocol implementation of {@link Caller}.
 *
 * @param <R>
 */
@Getter
@Accessors(fluent = true)
public class Http2Caller<R> extends HttpCaller<R> {

    Http2Caller(URL url, Http2CallerBuilder<R> builder) {
        super(url, builder);
    }

    public void call(StreamObserver<R> responseObserver, Object... args) {
        StreamReplyFuture future = StreamReplyFuture.from(responseObserver);
        RpcSupport.locateCall(this, future, args);
    }

    public <T> StreamObserver<T> call(StreamObserver<R> responseObserver) {
        StreamReplyFuture future = StreamReplyFuture.from(responseObserver);
        CallInvocation<?> invocation = RpcSupport.locateCall(this, future, null);
        Client client = protocol.openClient(invocation.clientUrl(), invocation.portal());
        Channel channel = client.channel();
        return new StreamSender<>(channel,invocation);
    }



    /**
     * Creates and returns a new {@link Http2CallerBuilder} instance for constructing
     * {@link Http2Caller} objects using a fluent API.
     *
     * @param returnType
     * @param <R>
     * @return
     */
    public static <R> Http2CallerBuilder<R> builder(TypeToken<R> returnType) {
        return new Http2CallerBuilder<>(returnType);
    }
}
