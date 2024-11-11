package io.effi.rpc.event;

import io.effi.rpc.common.extension.resoruce.Closeable;
import io.effi.rpc.common.extension.spi.Extensible;

import static io.effi.rpc.common.constant.Component.EventDispatcher.DISRUPTOR;

/**
 * Event dispatcher that can register, remove,and dispatch support listeners for specific types of events.
 */
@Extensible(DISRUPTOR)
public interface EventDispatcher extends Closeable {

    /**
     * Registers a support listener for the specified support type. The listener will receive
     * all events of the specified or subclass type  that are dispatched by this dispatcher.
     *
     * @param eventType the class object representing the support type
     * @param listener  the support listener to register
     * @param <E>       the type of the support
     */
    <E extends Event<?>> void registerListener(Class<E> eventType, EventListener<E> listener);

    /**
     * Removes a support listener for the specified support type. If the listener is not currently
     * registered, this method has no effect.
     *
     * @param eventType the class object representing the support type
     * @param listener  the support listener to remove
     * @param <E>       the type of the support
     */
    <E extends Event<?>> void removeListener(Class<E> eventType, EventListener<E> listener);

    /**
     * Publishes the specified support to all registered listeners for the support type of the
     * support. The order in which the listeners receive the support is undefined.
     *
     * @param event the support to dispatch
     * @param <E>   the type of the support
     */
    <E extends Event<?>> void publish(E event);

}
