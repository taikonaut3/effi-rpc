package io.effi.rpc.event.disruptor;

import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.event.EventDispatcher;
import io.effi.rpc.event.EventDispatcherFactory;

import static io.effi.rpc.common.constant.Component.EventDispatcher.DISRUPTOR;

/**
 * {@link EventDispatcherFactory} implementation based on disruptor.
 */
@Extension(DISRUPTOR)
public class DisruptorEventDispatcherFactory implements EventDispatcherFactory {
    @Override
    public EventDispatcher create(URL url) {
        return new DisruptorEventDispatcher(url);
    }
}
