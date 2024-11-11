package io.effi.rpc.transport;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.AttributeKey;
import io.effi.rpc.common.extension.RpcContext;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.StringUtil;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.metrics.CallerMetrics;
import io.effi.rpc.transport.client.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Future result of an RPC call, extending {@link CompletableFuture}.
 * <p>
 * This class manages the asynchronous handling of RPC responses, allowing clients to
 * retrieve results once they become available. It supports one-way communication and
 * keeps track of response consumers.
 * </p>
 */
@Getter
@Accessors(fluent = true)
public class RpcFuture extends CompletableFuture<Object> {

    public static final AttributeKey<RpcFuture> ATTRIBUTE_KEY = AttributeKey.valueOf("rpcFuture");

    // Map to hold active RpcFuture instances by their unique ID
    public static final Map<Long, RpcFuture> futures = new ConcurrentHashMap<>();

    // Atomic counter for generating unique IDs for futures
    private static final AtomicLong INCREASE = new AtomicLong(0);

    // List of consumers to be notified upon completion of the response
    private final List<Consumer<Response>> completeResponseConsumers = new LinkedList<>();

    // Unique ID for this RpcFuture
    private final long id;

    // URL associated with the invocation
    private final URL url;

    // The original invocation associated with this future
    private final CallInvocation<?> invocation;

    // Count of errors encountered during the RPC call
    private final AtomicInteger errorCount = new AtomicInteger(0);

    // Exception encountered during the RPC call, if any
    private RpcException exception;

    // The response received for the invocation
    private Response response;

    // Client associated with this RpcFuture
    @Setter
    private Client client;

    /**
     * Constructs a new RpcFuture instance for the specified invocation.
     *
     * @param invocation The invocation object associated with this future.
     */
    public RpcFuture(CallInvocation<?> invocation) {
        this.url = invocation.callerUrl();
        this.invocation = invocation;
        this.id = INCREASE.getAndIncrement();
        url.addParam(KeyConstant.UNIQUE_ID, String.valueOf(id));
        addFuture(id(), this);
        whenCompleteAsync((resp, ex) -> removeFuture(id()));
        boolean oneway = url.getBooleanParam(KeyConstant.ONEWAY);
        if (oneway) {
            complete(null);
        }
    }

    /**
     * Retrieves an RpcFuture associated with the given invocation, creating
     * a new one if necessary.
     *
     * @param invocation The invocation object to retrieve the future for.
     * @return The associated RpcFuture instance.
     */
    public static RpcFuture getFuture(CallInvocation<?> invocation) {
        URL url = invocation.callerUrl();
        String id = url.getParam(KeyConstant.UNIQUE_ID);
        if (StringUtil.isBlank(id)) {
            return new RpcFuture(invocation);
        }
        RpcFuture future = getFuture(Long.parseLong(id));
        if (future == null) {
            future = new RpcFuture(invocation);
        }
        return future;
    }

    /**
     * Retrieves an RpcFuture by its unique ID.
     *
     * @param id The unique ID of the RpcFuture to retrieve.
     * @return The RpcFuture instance, or null if not found.
     */
    public static RpcFuture getFuture(long id) {
        return futures.get(id);
    }

    /**
     * Retrieves an RpcFuture associated with the specified URL.
     *
     * @param url The URL to retrieve the associated RpcFuture for.
     * @return The RpcFuture instance, or null if not found.
     */
    public static RpcFuture getFuture(URL url) {
        String id = url.getParam(KeyConstant.UNIQUE_ID);
        if (!StringUtil.isBlank(id)) {
            return getFuture(Long.parseLong(id));
        }
        return null;
    }

    /**
     * Adds a future to the collection of active futures.
     *
     * @param id     The unique ID of the future.
     * @param future The RpcFuture instance to add.
     */
    public static void addFuture(long id, RpcFuture future) {
        futures.put(id, future);
    }

    /**
     * Removes a future from the collection of active futures.
     *
     * @param id The unique ID of the future to remove.
     */
    public static void removeFuture(long id) {
        futures.remove(id);
    }

    /**
     * Returns all uncompleted RpcFutures.
     *
     * @return A collection of uncompleted RpcFutures.
     */
    public static Collection<RpcFuture> unCompletedFutures() {
        return futures.values();
    }

    /**
     * Clears all futures, waiting for their completion.
     */
    public static void clear() {
        try {
            CompletableFuture.allOf(unCompletedFutures().toArray(new RpcFuture[0])).get();
        } catch (Exception e) {
            throw RpcException.wrap(e);
        }
    }

    /**
     * Returns the result of the future, blocking if necessary until it is available.
     *
     * @return The result of the future, or null if no value is returned.
     * @throws InterruptedException If the current thread is interrupted while waiting.
     * @throws ExecutionException If the computation threw an exception.
     */
    @Override
    public Object get() {
        try {
            return super.get(timeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            if (e instanceof TimeoutException) {
                CallerMetrics callerMetrics = invocation.invoker().get(CallerMetrics.ATTRIBUTE_KEY);
                callerMetrics.timeoutCount().increment();
                throw new RpcException(url.authority() + " protocol call timeout: " + timeout() + "ms", e);
            }
            throw RpcException.wrap(e);
        } finally {
            removeFuture(id);
            if (response != null) {
                RpcContext.currentContext().set(Response.ATTRIBUTE_KEY, response);
                RpcContext.ResponseContext.parse(response.url());
            }
        }
    }

    /**
     * Completes the future with the specified response and notifies all registered consumers.
     *
     * @param response The response to complete the future with.
     */
    public void completeResponse(Response response) {
        completeResponseConsumers.forEach(consumer -> consumer.accept(response));
        this.response = response;
        Result result = response.result();
        if (response.code() == Response.ERROR) {
            errorCount.incrementAndGet();
            exception = RpcException.wrap(result.exception());
        } else if (response.code() == Response.SUCCESS) {
            if (returnType() == response.getClass()) {
                complete(response);
            } else if (returnType() == response.message().getClass()) {
                complete(response.message());
            } else {
                complete(result.value());
            }
        }
    }

    /**
     * Registers a consumer to be notified when the response is complete.
     *
     * @param consumer The consumer to be notified.
     */
    public void whenCompleteResponse(Consumer<Response> consumer) {
        completeResponseConsumers.add(consumer);
    }

    /**
     * Returns the timeout duration for this RPC call.
     *
     * @return The timeout in milliseconds.
     */
    public int timeout() {
        return url.getIntParam(KeyConstant.TIMEOUT);
    }

    /**
     * Returns the return type of the method being invoked.
     *
     * @return The return type of the invocation.
     */
    public Type returnType() {
        return invocation.returnType();
    }

}

