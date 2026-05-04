package com.physmo.processor;

import com.physmo.core.FlowComponent;

import com.physmo.message.Msg;

/**
 * The MessageHandler interface represents a flow component responsible for processing a message
 * and transforming it. It defines a single method for handling messages.
 * <p>
 * Implementations of this interface can be used in a message flow to apply
 * specific transformations or logic to the content or headers of the message.
 * <p>
 * A MessageHandler operates on a message, encapsulated within the {@link Msg} object,
 * and returns a potentially modified or new message as output.
 */
public interface MessageHandler extends FlowComponent {
    Msg<?> handle(Msg<?> msg);
}
