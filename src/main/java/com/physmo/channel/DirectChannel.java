package com.physmo.channel;

import com.physmo.message.Msg;
import com.physmo.message.Subscriber;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a direct message channel that enables message delivery to all subscribed
 * recipients in a publish-subscribe model. Messages are processed in the order they
 * are sent, and each subscriber receives a copy of the message.
 * <p>
 * This class implements both {@code MessageChannel} and {@code SubscribableChannel} interfaces.
 * It supports message sending, subscription handling, and processing of messages
 * for all registered subscribers.
 * <p>
 * Features:
 * - Allows sending single messages or iterable collections of messages.
 * - Manages an internal queue for ordered message processing.
 * - Supports adding and removing subscribers dynamically.
 */
public class DirectChannel implements MessageChannel, SubscribableChannel {
    private final Queue<Msg<?>> queue = new ConcurrentLinkedQueue<>();
    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);

    @Override
    public void send(Msg<?> msg) {
        queue.add(msg);
        process();
    }

    public void process() {
        if (!isProcessing.compareAndSet(false, true)) {
            return;
        }

        try {
            Msg<?> msg;
            while ((msg = queue.poll()) != null) {
                for (Subscriber r : subscribers) {
                    try {
                        r.receive(msg);
                    } catch (Exception e) {
                        // Basic error logging. In a more complete system,
                        // this could be routed to an error channel.
                        System.err.println("Error delivering message to subscriber: " + e.getMessage());
                    }
                }
            }
        } finally {
            isProcessing.set(false);
            // Re-check for new messages added during loop termination
            if (!queue.isEmpty()) {
                process();
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
