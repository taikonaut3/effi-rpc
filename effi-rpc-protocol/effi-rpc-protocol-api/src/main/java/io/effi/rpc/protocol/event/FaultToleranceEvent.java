package io.effi.rpc.protocol.event;

import io.effi.rpc.event.AbstractEvent;
import io.effi.rpc.protocol.support.CompletableReplyFuture;

/**
 * FaultTolerance Event.
 */
public class FaultToleranceEvent extends AbstractEvent<CompletableReplyFuture> {

    private Throwable exception;

    public FaultToleranceEvent(CompletableReplyFuture future, Throwable exception) {
        super(future);
        this.exception = exception;
    }

    public Throwable exception() {
        return exception;
    }
}
