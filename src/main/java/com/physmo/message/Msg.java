package com.physmo.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Msg<T> {
    private final T payload;
    private final Map<String, Object> headers;

    public Msg(T payload, Map<String, Object> headers) {
        this.payload = payload;
        this.headers = headers;
    }

    public Msg(T payload) {
        this.payload = payload;
        this.headers = new HashMap<>();
    }

    public Msg(Msg<T> other) {
        this.payload = other.payload;
        this.headers = new HashMap<>(other.headers);
    }

    public Msg(Msg<T> other, Map<String, Object> extraHeaders) {
        this.payload = other.payload;
        this.headers = new HashMap<>(other.headers);
        this.headers.putAll(extraHeaders);
    }

    public Object getPayload() {
        return payload;
    }

    public Map<String, Object> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String toString() {
        return "Msg{" +
                "payload=" + payload +
                ", headers=" + headers +
                '}';
    }

}
