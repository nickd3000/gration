package com.physmo;

import com.physmo.message.Msg;

public interface Transformer extends FlowComponent {
    Msg<?> transform(Msg<?> msg);
}
