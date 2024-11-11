package io.effi.rpc.core.support;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.extension.Scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Base on {@link ScheduledExecutorService}.
 */
@Extension(Component.DEFAULT)
public class ScheduledThreadPool implements Scheduler {
    private final ScheduledExecutorService executorService;

    public ScheduledThreadPool() {
        this.executorService = Executors.newScheduledThreadPool(Constant.DEFAULT_CPU_THREADS);
    }

    @Override
    public void addDisposable(Runnable runnable, long delay, TimeUnit unit) {
        executorService.schedule(runnable, delay, unit);
    }

    @Override
    public void addPeriodic(Runnable runnable, long delay, long interval, TimeUnit unit) {
        executorService.scheduleAtFixedRate(runnable, delay, interval, unit);
    }

    @Override
    public void close() {
        executorService.shutdown();
    }

    @Override
    public boolean isActive() {
        return !executorService.isShutdown();
    }
}