package com.physmo.channel;

import com.physmo.message.Msg;

import java.util.Optional;

public interface PollableChannel {
    Optional<Msg<?>> poll();
}
