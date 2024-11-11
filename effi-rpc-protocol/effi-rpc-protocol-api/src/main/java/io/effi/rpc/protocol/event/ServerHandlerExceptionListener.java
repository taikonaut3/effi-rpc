package io.effi.rpc.protocol.event;

import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.event.EventListener;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.transport.Protocol;

/**
 * ServerHandlerExceptionListener.
 */
public class ServerHandlerExceptionListener implements EventListener<ServerHandlerExceptionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandlerExceptionListener.class);

    @Override
    public void onEvent(ServerHandlerExceptionEvent event) {
        URL url = event.channel().url();
        Throwable cause = event.source();
        logger.error("Server: {} exception: {}", cause, event.channel(), cause.getMessage());
        Protocol protocol = ExtensionLoader.loadExtension(Protocol.class, url.protocol());
        //protocol.sendResponse(event.channel(), null, new Result(url, cause));
    }

}
