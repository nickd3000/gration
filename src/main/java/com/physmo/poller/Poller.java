package com.physmo.poller;

public abstract class Poller {

    Runnable pollingAction;


    abstract void init();

    /**
     * Sets the action to be executed periodically as part of the polling process.
     * The provided action is scheduled to execute at a fixed rate, defined by the
     * rate and initial delay parameters of the current instance.
     *
     * @param pollingAction the {@code Runnable} task to be executed periodically.
     */
    public void setPollingAction(Runnable pollingAction) {
        this.pollingAction = pollingAction;
        init();
    }

    public void triggerPollingAction() {
        this.pollingAction.run();
    }
}
