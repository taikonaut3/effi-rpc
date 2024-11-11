package io.effi.rpc.protocol.event;

import io.effi.rpc.event.AbstractEvent;

/**
 * Send Message Event.
 */
public class SendMessageEvent extends AbstractEvent<Runnable> {

    public SendMessageEvent(Runnable runnable) {
        super(runnable);
    }

}
