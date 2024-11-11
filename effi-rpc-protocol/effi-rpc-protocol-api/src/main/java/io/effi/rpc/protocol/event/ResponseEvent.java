package io.effi.rpc.protocol.event;

import io.effi.rpc.event.AbstractEvent;
import io.effi.rpc.transport.Response;

/**
 * ResponseEvent.
 */
public class ResponseEvent extends AbstractEvent<Response> {

    public ResponseEvent(Response response) {
        super(response);
    }

}
