package com.physmo.channel;

import com.physmo.message.Msg;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class QueueChannel implements MessageChannel,PollableChannel {
    private final Queue<Msg<?>> queue = new LinkedList<>();

    @Override
    public void send(Msg<?> message) {
        queue.add(message);
    }

    @Override
    public Optional<Msg<?>> poll() {
        if (!queue.isEmpty()) return Optional.of(queue.poll());
        return Optional.empty();
    }

}
