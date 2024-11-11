package io.effi.rpc.event;

import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.common.url.URL;

import static io.effi.rpc.common.constant.Component.EventDispatcher.DISRUPTOR;

/**
 * Factory for creating EventDispatcher instances.
 */
@Extensible(DISRUPTOR)
public interface EventDispatcherFactory {

    /**
     * Creates an EventDispatcher for the specified URL.
     *
     * @param url the URL associated with the EventDispatcher
     * @return a new EventDispatcher instance
     */
    EventDispatcher create(URL url);
}

