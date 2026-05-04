package com.physmo.message;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageBuilder is a fluent API for creating {@link Msg} instances.
 * it allows for easy payload assignment and header manipulation before
 * constructing the final message object.
 *
 * @param <T> the type of the payload
 */
public class MessageBuilder<T> {
    private T payload;
    private final Map<String, Object> headers = new HashMap<>();

    private MessageBuilder(T payload) {
        this.payload = payload;
    }

    /**
     * Creates a new MessageBuilder with the specified payload.
     *
     * @param payload the payload for the message
     * @param <T>     the type of the payload
     * @return a new MessageBuilder instance
     */
    public static <T> MessageBuilder<T> withPayload(T payload) {
        return new MessageBuilder<>(payload);
    }

    /**
     * Creates a new MessageBuilder by copying the payload and headers from an existing message.
     *
     * @param message the message to copy from
     * @param <T>     the type of the payload
     * @return a new MessageBuilder instance
     */
    public static <T> MessageBuilder<T> fromMessage(Msg<T> message) {
        MessageBuilder<T> builder = new MessageBuilder<>((T) message.getPayload());
        builder.copyHeaders(message);
        return builder;
    }

    /**
     * Sets a header on the message being built.
     *
     * @param headerName  the name of the header
     * @param headerValue the value of the header
     * @return this MessageBuilder instance for chaining
     */
    public MessageBuilder<T> setHeader(String headerName, Object headerValue) {
        this.headers.put(headerName, headerValue);
        return this;
    }

    /**
     * Copies all headers from the provided map into the message being built.
     *
     * @param headersToCopy a map of headers to copy
     * @return this MessageBuilder instance for chaining
     */
    public MessageBuilder<T> copyHeaders(Map<String, Object> headersToCopy) {
        if (headersToCopy != null) {
            this.headers.putAll(headersToCopy);
        }
        return this;
    }

    /**
     * Copies all headers from the provided message into the message being built.
     *
     * @param message the message whose headers should be copied
     * @return this MessageBuilder instance for chaining
     */
    public MessageBuilder<T> copyHeaders(Msg<?> message) {
        if (message != null) {
            this.headers.putAll(message.getHeaders());
        }
        return this;
    }

    /**
     * Removes a header from the message being built.
     *
     * @param headerName the name of the header to remove
     * @return this MessageBuilder instance for chaining
     */
    public MessageBuilder<T> removeHeader(String headerName) {
        this.headers.remove(headerName);
        return this;
    }

    /**
     * Constructs a new {@link Msg} instance with the configured payload and headers.
     *
     * @return a new Msg instance
     */
    public Msg<T> build() {
        return new Msg<>(payload, new HashMap<>(headers));
    }
}
