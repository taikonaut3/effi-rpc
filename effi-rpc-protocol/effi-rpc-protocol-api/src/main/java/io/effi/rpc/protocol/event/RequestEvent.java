package io.effi.rpc.protocol.event;

import io.effi.rpc.event.AbstractEvent;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.Request;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Request Event.
 * <p>Wrap the current channel、Invocation and request.</p>
 */
@Getter
@Accessors(fluent = true)
public class RequestEvent extends AbstractEvent<Request> {

    private final Channel channel;

    public RequestEvent(Request request, Channel channel) {
        super(request);
        this.channel = channel;
    }
}
