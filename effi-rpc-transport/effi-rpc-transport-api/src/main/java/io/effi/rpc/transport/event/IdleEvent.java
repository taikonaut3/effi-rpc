package io.effi.rpc.transport.event;

import io.effi.rpc.event.AbstractEvent;
import io.effi.rpc.transport.channel.Channel;

/**
 * HeartBeatEvent.
 */
public class IdleEvent extends AbstractEvent<Channel> {

    public IdleEvent(Channel channel) {
        super(channel);
    }

}
