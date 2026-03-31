package com.physmo.message;

import java.util.HashMap;
import java.util.Map;

public class MessageBuilder<T> {
    private T payload;
    private final Map<String, Object> headers = new HashMap<>();

    private MessageBuilder(T payload) {
        this.payload = payload;
    }

    public static <T> MessageBuilder<T> withPayload(T payload) {
        return new MessageBuilder<>(payload);
    }

    public static <T> MessageBuilder<T> fromMessage(Msg<T> message) {
        MessageBuilder<T> builder = new MessageBuilder<>((T) message.getPayload());
        builder.copyHeaders(message.getHeaders());
        return builder;
    }

    public MessageBuilder<T> setHeader(String headerName, Object headerValue) {
        this.headers.put(headerName, headerValue);
        return this;
    }

    public MessageBuilder<T> copyHeaders(Map<String, Object> headersToCopy) {
        if (headersToCopy != null) {
            this.headers.putAll(headersToCopy);
        }
        return this;
    }

    public MessageBuilder<T> removeHeader(String headerName) {
        this.headers.remove(headerName);
        return this;
    }

    public Msg<T> build() {
        return new Msg<>(payload, new HashMap<>(headers));
    }
}
