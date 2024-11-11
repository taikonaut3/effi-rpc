package io.effi.rpc.transport.event;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.event.EventListener;
import io.effi.rpc.transport.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The count is refreshed when the current channel information is transmitted.
 */
public class RefreshHeartBeatCountEventListener implements EventListener<RefreshHeartBeatCountEvent> {

    @Override
    public void onEvent(RefreshHeartBeatCountEvent event) {
        Channel channel = event.source();
        Integer writeIdeTimes = event.writeIdeTimes();
        Integer readIdeTimes = event.readIdeTimes();
        Integer allIdeTimes = event.allIdeTimes();
        if (writeIdeTimes != null) {
            channel.set(KeyConstant.WRITE_IDLE_TIMES, new AtomicInteger(writeIdeTimes));
        }
        if (readIdeTimes != null) {
            channel.set(KeyConstant.READER_IDLE_TIMES, new AtomicInteger(readIdeTimes));
        }
        if (allIdeTimes != null) {
            channel.set(KeyConstant.ALL_IDLE_TIMES, new AtomicInteger(allIdeTimes));
        }
    }

}
