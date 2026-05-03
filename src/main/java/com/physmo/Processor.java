package com.physmo;

import com.physmo.message.Msg;

import java.util.Collections;
import java.util.List;

/**
 * A unified interface for all flow components. It defines a single method for processing
 * a message and returning a list of resulting messages.
 * <p>
 * This interface enables a consistent way to handle various types of processing:
 * - 1-to-1 (e.g., MessageHandler, Transformer): returns a list with a single message.
 * - 1-to-0 (e.g., Filter returning false): returns an empty list.
 * - 1-to-N (e.g., Split): returns a list with multiple messages.
 * - Side-effects (e.g., Peek): returns a list with the original message.
 */
@FunctionalInterface
public interface Processor extends FlowComponent {
    List<Msg<?>> process(Msg<?> msg);

    /**
     * Adapts a MessageHandler to the Processor interface.
     */
    static Processor fromHandler(MessageHandler handler) {
        return msg -> {
            Msg<?> result = handler.handle(msg);
            return result != null ? List.of(result) : Collections.emptyList();
        };
    }

    /**
     * Adapts a Transformer to the Processor interface.
     */
    static Processor fromTransformer(Transformer transformer) {
        return msg -> {
            Msg<?> result = transformer.transform(msg);
            return result != null ? List.of(result) : Collections.emptyList();
        };
    }

    /**
     * Adapts a Filter to the Processor interface.
     */
    static Processor fromFilter(Filter filter) {
        return msg -> filter.filter(msg) ? List.of(msg) : Collections.emptyList();
    }

    /**
     * Adapts a Peek to the Processor interface.
     */
    static Processor fromPeek(Peek peek) {
        return msg -> {
            peek.peek(msg);
            return List.of(msg);
        };
    }

    /**
     * Adapts a Split to the Processor interface.
     */
    static Processor fromSplit(Split split) {
        return split::handle;
    }
}
