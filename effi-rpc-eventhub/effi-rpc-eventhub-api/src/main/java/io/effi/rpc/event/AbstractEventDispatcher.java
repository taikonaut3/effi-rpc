package io.effi.rpc.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract implementation of {@link EventDispatcher}.
 */
public abstract class AbstractEventDispatcher implements EventDispatcher {

    protected final Map<Class<? extends Event<?>>, List<EventListener<?>>> listenerMap = new ConcurrentHashMap<>();

    @Override
    public <E extends Event<?>> void registerListener(Class<E> eventType, EventListener<E> listener) {
        listenerMap.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public <E extends Event<?>> void removeListener(Class<E> eventType, EventListener<E> listener) {
        if (listenerMap.containsKey(eventType)) {
            List<EventListener<?>> listeners = listenerMap.get(eventType);
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listenerMap.remove(eventType);
            }
        }
    }

    @Override
    public <E extends Event<?>> void publish(E event) {
        if (event != null && event.allowPropagation()) {
            doPublish(event);
        }
    }

    protected abstract <E extends Event<?>> void doPublish(E event);

}
