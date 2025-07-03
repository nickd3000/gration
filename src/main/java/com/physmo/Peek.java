package com.physmo;

import com.physmo.message.Msg;

public interface Peek extends FlowComponent{
    void peek(Msg<?> msg);
}
