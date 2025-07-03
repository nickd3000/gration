package com.physmo;

import com.physmo.message.Msg;

/**
 * The Filter interface represents a flow component that determines whether a message
 * should proceed in a message flow based on a user-defined filter condition.
 * <p>
 * Implementations of this interface evaluate a given message and return a
 * boolean result. If the result is true, the message continues through the
 * flow. If the result is false, the message is filtered out and does not proceed further.
 * <p>
 * This interface is typically used in message processing pipelines to
 * selectively allow or block messages based on their content or metadata.
 */
public interface Filter extends FlowComponent {
    boolean filter(Msg<?> msg);
}
