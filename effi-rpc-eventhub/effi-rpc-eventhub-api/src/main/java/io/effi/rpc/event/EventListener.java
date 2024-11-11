package io.effi.rpc.event;

/**
 * Handles event of type {@link E}.This interface defines a single method that is invoked
 * when an event occurs.
 *
 * @param <E> the type of event that this listener can handle,
 *            extending {@link Event}
 */
@FunctionalInterface
public interface EventListener<E extends Event<?>> {

    /**
     * Invoked when an event of type {@link E} occurs.
     *
     * @param event the event that has occurred
     */
    void onEvent(E event);
}


