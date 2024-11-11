package io.effi.rpc.transport.channel;

import io.effi.rpc.common.exception.RpcException;

/**
 * Manage the lifecycle and data flow of a {@link Channel}.
 * <p>
 * Defines callback methods that are triggered during different events
 * such as connection, disconnection, message reception, and error handling.
 * </p>
 */
public interface ChannelHandler {

    /**
     * Invoked when a connection to the {@link Channel} is established.
     *
     * @param channel the channel that has been connected
     * @throws RpcException if any error occurs during the handling of the connection
     */
    default void connected(Channel channel) throws RpcException {
        // Default implementation can be overridden by implementing classes
    }

    /**
     * Invoked when the {@link Channel} is disconnected.
     *
     * @param channel the channel that has been disconnected
     * @throws RpcException if any error occurs during the handling of the disconnection
     */
    default void disconnected(Channel channel) throws RpcException {
        // Default implementation can be overridden by implementing classes
    }

    /**
     * Invoked when a message is received from the {@link Channel}.
     *
     * @param msg the message that was received through the channel
     * @throws RpcException if any error occurs while processing the received message
     */
    default void received(ChannelMessage msg) throws RpcException {
        // Default implementation can be overridden by implementing classes
    }

    /**
     * Invoked when a message is successfully sent through the {@link Channel}.
     *
     * @param msg the message that was sent through the channel
     * @throws RpcException if any error occurs while processing the sent message
     */
    default void sent(ChannelMessage msg) throws RpcException {
        // Default implementation can be overridden by implementing classes
    }

    /**
     * Invoked when an exception is caught in the {@link Channel}.
     *
     * @param channel the channel where the exception was caught
     * @param cause   the throwable representing the cause of the error
     * @throws RpcException if any error occurs during exception handling
     */
    default void caught(Channel channel, Throwable cause) throws RpcException {
        // Default implementation can be overridden by implementing classes
    }
}
