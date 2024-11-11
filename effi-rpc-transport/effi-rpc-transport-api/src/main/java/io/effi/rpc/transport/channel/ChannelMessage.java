package io.effi.rpc.transport.channel;

/**
 * Wrapper for {@link Channel} and its associated message.
 * Allows both reading and modifying the message during the processing
 * in a {@link ChannelHandler} chain.
 */
public class ChannelMessage {

    private final Channel channel;

    private Object message;

    /**
     * Constructs a new {@link ChannelMessage} wrapping the given {@link Channel} and message.
     *
     * @param channel the channel to associate with the message
     * @param message the initial message to wrap
     */
    public ChannelMessage(Channel channel, Object message) {
        this.channel = channel;
        this.message = message;
    }

    /**
     * Returns the current message.
     *
     * @return the current message object
     */
    public Object message() {
        return message;
    }

    /**
     * Sets a new message, allowing it to be modified during handler execution.
     *
     * @param message the new message to set
     */
    public void message(Object message) {
        this.message = message;
    }

    /**
     * Returns the channel associated with this message.
     *
     * @return the {@link Channel}
     */
    public Channel channel() {
        return channel;
    }
}

