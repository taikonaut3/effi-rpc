package io.effi.rpc.protocol.event;

import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.transport.channel.Channel;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * ClientHandlerExceptionEvent.
 */
@Getter
@Accessors(fluent = true)
public class ClientHandlerExceptionEvent extends ChannelHandlerExceptionEvent {

    private final ReplyFuture future;

    public ClientHandlerExceptionEvent(ReplyFuture future, Channel channel, Throwable cause) {
        super(channel, cause);
        this.future = future;
    }

}
