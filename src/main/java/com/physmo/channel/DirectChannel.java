package com.physmo.channel;

import com.physmo.message.Msg;
import com.physmo.message.Subscriber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a direct message channel that enables message delivery to all subscribed
 * recipients in a publish-subscribe model. Messages are processed in the order they
 * are sent, and each subscriber receives a copy of the message.
 *
 * This class implements both {@code MessageChannel} and {@code SubscribableChannel} interfaces.
 * It supports message sending, subscription handling, and processing of messages
 * for all registered subscribers.
 *
 * Features:
 * - Allows sending single messages or iterable collections of messages.
 * - Manages an internal queue for ordered message processing.
 * - Supports adding and removing subscribers dynamically.
 */
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

    public void subscribe(Subscriber r) {
        subscribers.add(r);
    }

    public void unsubscribe(Subscriber r) {
        subscribers.remove(r);
    }

    @Override
    public void addSubscriber(Subscriber r) {
        subscribe(r);
    }

}
