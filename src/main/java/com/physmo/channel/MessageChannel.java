package com.physmo.channel;

import com.physmo.message.Msg;

/**
 * Represents a communication mechanism for sending messages. It serves as an
 * abstraction that allows messages to be sent to a channel without specifying
 * how the messages are processed or stored.
 * <BR><BR>
 * Classes implementing this interface are responsible for defining the behavior
 * of how messages are handled after they are sent to the channel.
 * <BR><BR>
 * Features:
 * - Allows sending messages of type {@code Msg<?>}.
 * - Behavior and processing of messages depend on the specific implementation.
 * - Enables message-based interactions between different components.
 * <BR><BR>
 * Common usage scenarios may include:
 * - Queuing messages for delivery and processing.
 * - Publishing messages to multiple subscribers.
 * - Providing integration points for message-driven applications.
 */
public interface MessageChannel {
    void send(Msg<?> message);
}
