package io.effi.rpc.event.flow;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.event.AbstractEventDispatcher;
import io.effi.rpc.event.Event;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.event.EventDispatcher;
import io.effi.rpc.event.EventListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;

@Extension(Component.EventDispatcher.FLOW)
@Deprecated
public class DefaultEventDispatcher extends AbstractEventDispatcher implements EventDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEventDispatcher.class);

    private final Map<EventListener<?>, DefaultEventSubscriber<?>> subscriberMap = new ConcurrentHashMap<>();

    private final DefaultPublisher<Event<?>> publisher;

    public DefaultEventDispatcher() {
        publisher = new DefaultPublisher<>();
    }

    public DefaultEventDispatcher(Executor executor) {
        publisher = new DefaultPublisher<>(executor);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Event<?>> void registerListener(Class<E> eventType, EventListener<E> listener) {
        super.registerListener(eventType, listener);
        DefaultEventSubscriber<?> subscriber = new DefaultEventSubscriber<>(listener);
        publisher.subscribe((Flow.Subscriber<? super Event<?>>) subscriber);
        subscriberMap.put(listener, subscriber);
        logger.debug("Register Listener for Event[{}],Listener: {}", eventType.getSimpleName(), listener.getClass().getSimpleName());
    }

    @Override
    public <E extends Event<?>> void removeListener(Class<E> eventType, EventListener<E> listener) {
        super.removeListener(eventType, listener);
        DefaultEventSubscriber<?> subscriber = subscriberMap.get(listener);
        subscriber.cancel();
        logger.debug("Remove Listener for Event[{}],Listener: {}", eventType.getSimpleName(), listener.getClass().getSimpleName());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <E extends Event<?>> void doPublish(E event) {
        List<EventListener<?>> listeners = listenerMap.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(event.getClass()))
                .flatMap(entry -> entry.getValue().stream())
                .toList();
        for (EventListener<?> listener : listeners) {
            logger.debug("Publish Event({}) to {}", event, listener);
            publisher.publish(event, (Flow.Subscriber<? super Event<?>>) subscriberMap.get(listener));
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
