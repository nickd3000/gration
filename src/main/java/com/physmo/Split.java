package com.physmo;

import com.physmo.message.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Split} class represents a flow component that processes a message
 * and splits its payload if the payload is a list. Each element in the list
 * becomes a separate message with the same headers as the original message.
 *
 * This class is designed to enable the transformation of a single message
 * with a list payload into multiple messages, facilitating further processing
 * of those individual elements downstream.
 */
public class Split implements FlowComponent {
    List<Msg<?>> outList = new ArrayList<>();

    List<Msg<?>> handle(Msg<?> msg) {
        if (msg.getPayload() instanceof List<?>) {
            for (Object element : (List<?>) msg.getPayload()) {
                Msg<?> newMsg = new Msg<>(element, msg.getHeaders());
                outList.add(newMsg);
            }
        } else {
            outList.add(msg);
        }
        return outList;
    }
}
