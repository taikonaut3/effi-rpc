package io.effi.rpc.protocol.support;

import io.effi.rpc.core.Portal;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.protocol.event.FaultToleranceEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class CompletableReplyFuture extends ReplyFuture {

    private final CompletableFuture<Object> completableFuture;

    private final AtomicInteger errorCount = new AtomicInteger(0);

    private Throwable exception;

    public CompletableReplyFuture() {
        this.completableFuture = new CompletableFuture<>();
    }

    @Override
    public void whenComplete(Consumer<Result> consumer) {
        if (errorCount.get() == 0) {
            completedConsumers.add(consumer);
        }
    }

    @Override
    protected void doComplete(Object value) {
        completableFuture.complete(value);
    }

    @Override
    protected void doCompleteExceptionally(Throwable t) {
        Portal portal = invocation.caller().portal();
        portal.publishEvent(new FaultToleranceEvent(this, t));
        // completableFuture.completeExceptionally(t);
    }

    public AtomicInteger errorCount() {
        return errorCount;
    }

    public CompletableFuture<Object> completableFuture() {
        return completableFuture;
    }
}
