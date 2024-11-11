package io.effi.rpc.protocol.event;

import io.effi.rpc.transport.channel.Channel;

/**
 * ServerHandler ExceptionEvent.
 */
public class ServerHandlerExceptionEvent extends ChannelHandlerExceptionEvent {

    public ServerHandlerExceptionEvent(Channel channel, Throwable cause) {
        super(channel, cause);
    }

}
