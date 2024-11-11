package io.effi.rpc.core.config;

import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.common.url.Parameter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * EventDispatcher Config.
 */
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class EventDispatcherConfig extends UrlTypeConfig {

    @Parameter(KeyConstant.BUFFER_SIZE)
    private int bufferSize = Constant.DEFAULT_BUFFER_SIZE;

    @Parameter(KeyConstant.SUBSCRIBES)
    private int subscribes = Constant.DEFAULT_SUBSCRIBES;

    public EventDispatcherConfig() {
        type(Constant.DEFAULT_EVENT_DISPATCHER);
    }

    @Override
    public URL toUrl() {
        return URL.builder()
                .protocol(type)
                .address(NetUtil.toAddress(NetUtil.defaultHost(), 0))
                .params(parameterization())
                .build();
    }
}
