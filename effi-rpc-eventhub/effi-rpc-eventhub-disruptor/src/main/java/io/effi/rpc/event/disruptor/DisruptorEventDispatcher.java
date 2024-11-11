package io.effi.rpc.event.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.executor.RpcThreadFactory;
import io.effi.rpc.common.extension.Holder;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.event.AbstractEventDispatcher;
import io.effi.rpc.event.Event;
import io.effi.rpc.event.EventListener;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;

/**
 * Disruptor based support dispatcher.
 */
@Extension(Component.EventDispatcher.DISRUPTOR)
public class DisruptorEventDispatcher extends AbstractEventDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DisruptorEventDispatcher.class);

    private final URL url;

    private final RingBuffer<EventHolder<?>> ringBuffer;

    private Disruptor<EventHolder<?>> disruptor;

    private volatile boolean started;

    public DisruptorEventDispatcher(URL url) {
        this.url = url;
        this.ringBuffer = buildRingBuffer();
    }

    @Override
    public <E extends Event<?>> void registerListener(Class<E> eventType, EventListener<E> listener) {
        super.registerListener(eventType, listener);
        logger.debug("Registered {}<{}>", ObjectUtil.simpleClassName(listener), ObjectUtil.simpleClassName(eventType));
    }

    @Override
    public <E extends Event<?>> void removeListener(Class<E> eventType, EventListener<E> listener) {
        super.removeListener(eventType, listener);
        logger.debug("Remove {}<{}>", ObjectUtil.simpleClassName(listener), ObjectUtil.simpleClassName(eventType));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <E extends Event<?>> void doPublish(E event) {
        ringBuffer.publishEvent((eventHolder, sequence) -> {
            EventHolder<E> holder = (EventHolder<E>) eventHolder;
            holder.set(event);
        });
        logger.trace("Publish <{}>", ObjectUtil.simpleClassName(event));
    }

    private RingBuffer<EventHolder<?>> buildRingBuffer() {
        int bufferSize = Constant.DEFAULT_BUFFER_SIZE;
        int subscribes = Constant.DEFAULT_SUBSCRIBES;
        if (url != null) {
            bufferSize = url.getIntParam(KeyConstant.BUFFER_SIZE, Constant.DEFAULT_BUFFER_SIZE);
            subscribes = url.getIntParam(KeyConstant.SUBSCRIBES, Constant.DEFAULT_SUBSCRIBES);
        }
        disruptor = new Disruptor<>(EventHolder::new, bufferSize, new RpcThreadFactory("disruptor-event-handler"));
        RingBuffer<EventHolder<?>> ringBuffer = disruptor.getRingBuffer();
        DisruptorEventHandler<?>[] handlers = new DisruptorEventHandler<?>[subscribes];
        for (int i = 0; i < subscribes; i++) {
            handlers[i] = new DisruptorEventHandler<>();
        }
        disruptor.handleEventsWith(handlers);
        disruptor.setDefaultExceptionHandler(new DisruptorExceptionHandler());
        disruptor.start();
        started = true;
        return ringBuffer;
    }

    @Override
    public synchronized void close() {
        if (disruptor != null) {
            disruptor.shutdown();
        }
        started = false;
    }

    @Override
    public boolean isActive() {
        return started;
    }

    static final class DisruptorExceptionHandler implements ExceptionHandler<EventHolder<?>> {

        @Override
        public void handleEventException(Throwable ex, long sequence, EventHolder<?> event) {
            logger.error("{} handle <{}> failed", ex, ObjectUtil.simpleClassName(this), ObjectUtil.simpleClassName(event.get()));
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            logger.error(ObjectUtil.simpleClassName(this) + " start started", ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            logger.error(ObjectUtil.simpleClassName(this) + " shutdown failed", ex);
        }
    }

    static final class EventHolder<E extends Event<?>> extends Holder<E> {
    }

    class DisruptorEventHandler<E extends Event<?>> implements EventHandler<EventHolder<E>> {

        @SuppressWarnings("unchecked")
        @Override
        public void onEvent(EventHolder<E> holder, long sequence, boolean endOfBatch) throws Exception {
            E event = holder.get();
            if (event.stopPropagation()) {
                listenerMap.entrySet().stream().filter(entry -> entry.getKey().isAssignableFrom(event.getClass())).forEach(entry -> entry.getValue().forEach(item -> {
                    EventListener<E> listener = (EventListener<E>) item;
                    try {
                        listener.onEvent(event);
                        logger.trace("{} handle <{}>", ObjectUtil.simpleClassName(listener), ObjectUtil.simpleClassName(event));
                    } catch (Exception e) {
                        logger.error("{} handle <{}> failed", e, ObjectUtil.simpleClassName(event), ObjectUtil.simpleClassName(event));
                        throw RpcException.wrap(e);
                    } finally {
                        event.stopPropagation();
                    }
                }));
            }
        }
    }

}

