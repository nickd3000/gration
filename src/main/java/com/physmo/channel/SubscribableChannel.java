package com.physmo.channel;

import com.physmo.message.Subscriber;

public interface SubscribableChannel {
    void addSubscriber(Subscriber subscriber);
}
