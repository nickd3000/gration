package com.physmo;

import com.physmo.message.Msg;

/**
 * The Peek interface represents a flow component designed to inspect messages
 * passing through a message flow without modifying them. Its primary purpose
 * is for debugging or monitoring the message payload and headers during the
 * flow execution.
 * <p>
 * Implementations of this interface must define the behavior of the peek
 * operation, which processes a message and performs an action, such as logging
 * or collecting metrics.
 * <p>
 * The Peek operation does not alter the message content or influence the flow
 * logic. Instead, it allows for observing messages as they transit through the
 * system.
 */
public interface Peek extends FlowComponent{
    void peek(Msg<?> msg);
}
