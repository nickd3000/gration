package com.physmo;

import com.physmo.message.Msg;

public interface Handler extends FlowComponent {
    Msg<?> handle(Msg<?> msg);
}
