package com.physmo.channel;

import com.physmo.message.Msg;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a pollable message channel that stores messages in an internal queue.
 * Messages are sent to the queue and can be retrieved later using the poll method.
 * <p>
 * This class implements both {@code MessageChannel} and {@code PollableChannel} interfaces.
 * It is suitable for point-to-point communication where messages are buffered
 * until a consumer is ready to process them.
 * <p>
 * Features:
 * - Thread-safe message storage using {@link ConcurrentLinkedQueue}.
 * - Supports the {@code send} operation to add messages to the queue.
 * - Supports the {@code poll} operation to retrieve and remove messages from the queue.
 */
public class QueueChannel implements MessageChannel, PollableChannel {
    private final Queue<Msg<?>> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void send(Msg<?> message) {
        queue.add(message);
    }

    @Override
    public Optional<Msg<?>> poll() {
        return Optional.ofNullable(queue.poll());
    }

}
