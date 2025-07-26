package com.physmo;

import com.physmo.message.Msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code Split} class represents a flow component that processes a message
 * and splits its payload if the payload is a list. Each element in the list
 * becomes a separate message with the same headers as the original message.
 * <br>
 * This class is designed to enable the transformation of a single message
 * with a list payload into multiple messages, facilitating further processing
 * of those individual elements downstream.
 */
public class Split implements FlowComponent {
    private final List<Msg<?>> outList = new ArrayList<>();

    List<Msg<?>> handle(Msg<?> msg) {
        outList.clear(); // Ensure a clean state for each handle operation
        
        if (isListPayload(msg)) {
            splitListPayload(msg);
        } else {
            outList.add(msg);
        }
        
        return outList;
    }

    private boolean isListPayload(Msg<?> msg) {
        return msg.getPayload() instanceof List<?>;
    }

    private void splitListPayload(Msg<?> msg) {
        List<?> payload = (List<?>) msg.getPayload();
        for (int index = 0; index < payload.size(); index++) {
            Object element = payload.get(index);
            Msg<?> newMsg = createSplitMessage(element, msg.getHeaders(), index);
            outList.add(newMsg);
        }
    }

    private Msg<?> createSplitMessage(Object payload, Map<String, Object> originalHeaders, int index) {
        Map<String, Object> headers = new HashMap<>(originalHeaders);
        headers.put("splitIndex", index);
        return new Msg<>(payload, headers);
    }
}