package io.effi.rpc.protocol.event;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.event.EventListener;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;

/**
 * ClientHandlerExceptionListener.
 */
public class ClientHandlerExceptionListener implements EventListener<ClientHandlerExceptionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandlerExceptionListener.class);

    @Override
    public void onEvent(ClientHandlerExceptionEvent event) {
        URL url = event.channel().url();
        Throwable cause = event.source();
        logger.error("Client: {} exception: {}", cause, event.channel(), cause.getMessage());
        event.future().complete(new Result(url, cause));
    }

}
