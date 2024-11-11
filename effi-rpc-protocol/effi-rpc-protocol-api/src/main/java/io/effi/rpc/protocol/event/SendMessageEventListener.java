package io.effi.rpc.protocol.event;

import io.effi.rpc.common.executor.RpcThreadPool;
import io.effi.rpc.event.EventListener;

import java.util.concurrent.ExecutorService;

/**
 * Send Message EventListener.
 */
public class SendMessageEventListener implements EventListener<SendMessageEvent> {

    private final ExecutorService executor = RpcThreadPool.defaultCPUExecutor("message-sender");

    @Override
    public void onEvent(SendMessageEvent event) {
        if (!executor.isShutdown()) {
            executor.execute(event.source());
        } else {
            event.source().run();
        }
    }
}
