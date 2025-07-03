package com.physmo.message;

/**
 * Represents an entity that can receive messages in a message-driven system.
 * A Subscriber is responsible for consuming messages of type {@code Msg<?>}.
 * Typically used in scenarios where messages are communicated between components
 * through channels, with Subscribers acting as endpoints or intermediaries.
 * <p>
 * Implementing classes should define the handling logic for incoming messages
 * in the {@code receive} method.
 */
public interface Subscriber {
    void receive(Msg<?> msg);
}
