package com.physmo.channel;

import com.physmo.message.Msg;
import com.physmo.message.Subscriber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DirectChannel implements MessageChannel, SubscribableChannel {
    private final Queue<Msg<?>> queue = new LinkedList<>();
    private final List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void send(Msg<?> msg) {
        queue.add(msg);
        process();
    }

    public void process() {
        while (!queue.isEmpty()) {
            Msg<?> msg = queue.poll();
            if (msg != null) {
                for (Subscriber r : subscribers) {
                    r.receive(msg);
                }
            }
        }
    }

    public void send(Iterable<Msg<?>> messages) {
        for (Msg<?> msg : messages) {
            queue.add(msg);
        }

        process();
    }

    @Override
    public void addSubscriber(Subscriber r) {
        subscribers.add(r);
    }

    public void removeSubscriber(Subscriber r) {
        subscribers.remove(r);
    }

}
