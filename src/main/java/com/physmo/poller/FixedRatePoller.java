package com.physmo.poller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A {@code FixedRatePoller} is an implementation of the {@link Poller} interface that schedules
 * periodic polling actions at fixed intervals. The polling rate and initial delay can be customized.
 * <BR><BR>
 * The class uses a {@link ScheduledExecutorService} to execute the polling logic on a separate thread.
 * <BR><BR>
 * Key Features:
 * - Allows specifying a fixed rate in milliseconds for periodic polling.
 * - Optionally supports an initial delay before starting the polling actions.
 * - The action to be executed periodically can be set via the {@code setPollingAction} method.
 * <BR><BR>
 * Constructor Details:
 * - {@code FixedRatePoller(long rateInMillis)}: Creates a FixedRatePoller with a specified rate in milliseconds.
 * - {@code FixedRatePoller(long rateInMillis, long initialDelayInMillis)}: Creates a FixedRatePoller
 * with a specified rate and an optional initial delay in milliseconds.
 */
public class FixedRatePoller extends Poller {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;

    long rateInMillis;
    long initialDelayInMillis = 0;

    /**
     * Constructs a {@code FixedRatePoller} instance with the specified polling rate.
     * The polling rate determines the fixed interval in milliseconds between
     * successive executions of the polling action.
     *
     * @param rateInMillis the rate in milliseconds at which the polling action will execute
     */
    public FixedRatePoller(long rateInMillis) {
        this.rateInMillis = rateInMillis;
    }

    /**
     * Constructs a {@code FixedRatePoller} instance with the specified polling rate and initial delay.
     * This constructor allows for scheduling periodic polling actions with a fixed interval and
     * an optional initial delay before the first execution.
     *
     * @param rateInMillis         the rate in milliseconds at which the polling action will execute
     * @param initialDelayInMillis the initial delay in milliseconds before the first execution of the polling action
     */
    public FixedRatePoller(long rateInMillis, long initialDelayInMillis) {
        this.rateInMillis = rateInMillis;
        this.initialDelayInMillis = initialDelayInMillis;
    }

    @Override
    public void init() {
        scheduledFuture = scheduler.scheduleWithFixedDelay(this::triggerPollingAction,
                initialDelayInMillis,
                rateInMillis,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        scheduler.shutdown();
    }

}
