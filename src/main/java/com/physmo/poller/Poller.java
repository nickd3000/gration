package com.physmo.poller;

public interface Poller {

    void setPollingAction(Runnable pollingAction);

    void triggerPollingAction();
}
