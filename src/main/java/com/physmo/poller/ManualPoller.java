package com.physmo.poller;


public class ManualPoller extends Poller {


    public ManualPoller() {

    }

    public void poll() {
        this.pollingAction.run();
    }

    @Override
    public void init() {
    }


}
