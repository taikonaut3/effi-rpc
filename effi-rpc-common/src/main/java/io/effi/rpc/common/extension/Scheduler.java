package io.effi.rpc.common.extension;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.extension.resoruce.Closeable;
import io.effi.rpc.common.extension.spi.Extensible;

import java.util.concurrent.TimeUnit;

/**
 * Used for publishing disposable or periodic tasks.
 */
@Extensible(Component.DEFAULT)
public interface Scheduler extends Closeable {

    /**
     * Publishing disposable task.
     *
     * @param runnable
     * @param delay
     * @param unit
     */
    void addDisposable(Runnable runnable, long delay, TimeUnit unit);

    /**
     * Publishing periodic task.
     *
     * @param runnable
     * @param delay
     * @param interval
     * @param unit
     */
    void addPeriodic(Runnable runnable, long delay, long interval, TimeUnit unit);
}
