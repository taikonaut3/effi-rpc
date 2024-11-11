package io.effi.rpc.protocol.event;

import io.effi.rpc.event.AbstractEvent;
import io.effi.rpc.transport.channel.Channel;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Abstract ChannelHandlerExceptionEvent for server and client.
 */
@Getter
@Accessors(fluent = true)
public abstract class ChannelHandlerExceptionEvent extends AbstractEvent<Throwable> {

    private final Channel channel;

    public ChannelHandlerExceptionEvent(Channel channel, Throwable cause) {
        super(cause);
        this.channel = channel;
    }

}
