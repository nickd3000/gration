package com.physmo;

import com.physmo.message.Msg;

public interface Filter extends FlowComponent {
    boolean filter(Msg<?> msg);
}
