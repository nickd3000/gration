package com.physmo.processor;

import com.physmo.core.FlowComponent;

import com.physmo.message.Msg;

public interface Transformer extends FlowComponent {
    Msg<?> transform(Msg<?> msg);
}
