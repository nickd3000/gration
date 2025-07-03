package com.physmo.channel;

import com.physmo.message.Msg;

public interface MessageChannel {
    void send(Msg<?> message);
}
