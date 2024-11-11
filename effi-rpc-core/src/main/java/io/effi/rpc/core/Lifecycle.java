package io.effi.rpc.core;

/**
 * Lifecycle interface that specifies the initialization, startup, and stop methods for a component.
 */
public interface Lifecycle {

    /**
     * Initializes the component. This method is typically called when the component is created.
     */
    default void init() {

    }

    /**
     * Starts the component. This method is typically called when the component is ready to run.
     */
    default void start() {

    }

    /**
     * Stops the component. This method is typically called when the component is shutting down.
     */
    default void stop() {

    }

    /**
     * The states of a process or component lifecycle.
     */
    enum State {

        /**
         * Indicates the component has been initialized but not yet started.
         */
        INITIALIZED,

        /**
         * Indicates the component is actively running.
         */
        STARTED,
        /**
         * Indicates the component has been stopped.
         */
        STOPPED
    }

}

