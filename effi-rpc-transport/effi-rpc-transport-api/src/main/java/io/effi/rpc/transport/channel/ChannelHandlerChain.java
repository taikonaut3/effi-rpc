package io.effi.rpc.transport.channel;

/**
 * Chain of {@link ChannelHandler}s, where each handler is executed in the order it was added.
 */
public interface ChannelHandlerChain extends ChannelHandler {

    /**
     * Adds a new {@link ChannelHandler} to the end of the chain.
     * Handlers added later will be executed after those that were added before.
     *
     * @param channelHandler the handler to be added to the chain
     * @return the current instance of {@link ChannelHandlerChain} for method chaining
     */
    ChannelHandlerChain addLast(ChannelHandler channelHandler);

    /**
     * Returns all {@link ChannelHandler}s currently in the chain.
     * The handlers are returned in the order they were added.
     *
     * @return an array of {@link ChannelHandler}s in the chain
     */
    ChannelHandler[] channelHandlers();
}

