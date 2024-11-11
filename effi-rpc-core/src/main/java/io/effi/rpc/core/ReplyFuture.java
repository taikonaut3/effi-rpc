package io.effi.rpc.core;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.result.Result;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public abstract class ReplyFuture {

    public static final AttributeKey<ReplyFuture> ATTRIBUTE_KEY = AttributeKey.valueOf("replyFuture");

    public static final Map<Long, ReplyFuture> FUTURES = new ConcurrentHashMap<>();

    // Atomic counter for generating unique IDs for futures
    private static final AtomicLong INCREASE = new AtomicLong(0);

    protected final List<Consumer<Result>> completedConsumers = new LinkedList<>();

    protected final long id;

    protected CallInvocation<?> invocation;

    protected ReplyFuture() {
        this.id = INCREASE.incrementAndGet();
        FUTURES.put(id, this);
    }

    public long id() {
        return id;
    }

    public void currentInvocation(CallInvocation<?> invocation) {
        this.invocation = invocation;
        invocation.callerUrl().addParam(KeyConstant.UNIQUE_ID, String.valueOf(id));
    }

    public void complete(Result result) {
        invokeCompletedConsumers(result);
        if (result.hasException()) {
            doCompleteExceptionally(result.exception());
        } else {
            doComplete(result.value());
        }
    }

    public void remove() {
        removeFuture(id);
    }

    public abstract void whenComplete(Consumer<Result> consumer);

    protected abstract void doComplete(Object value);

    protected abstract void doCompleteExceptionally(Throwable t);

    public static ReplyFuture acquireFuture(URL url) {
        return acquireFuture(url.getLongParam(KeyConstant.UNIQUE_ID));
    }

    public static ReplyFuture acquireFuture(long id) {
        return FUTURES.get(id);
    }

    public static void removeFuture(long id) {
        FUTURES.remove(id);
    }

    private void invokeCompletedConsumers(Result result) {
        completedConsumers.forEach(consumer -> consumer.accept(result));
    }

    public CallInvocation<?> invocation() {
        return invocation;
    }

    public boolean completed() {
        return false;
    }
}
