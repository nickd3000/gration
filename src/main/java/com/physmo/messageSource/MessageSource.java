package com.physmo.messageSource;

import com.physmo.message.Msg;

import java.util.Optional;

// Allow us to poll systems for messages that we create
public interface MessageSource<T> {
    Optional<Msg<T>> poll();
}
