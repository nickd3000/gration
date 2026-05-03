package com.physmo.channel;

import com.physmo.message.Msg;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueChannel implements MessageChannel,PollableChannel {
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
