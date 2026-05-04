package com.physmo.reference;

import com.physmo.MessageFlow;
import com.physmo.channel.DirectChannel;
import com.physmo.message.MessageBuilder;
import com.physmo.message.Msg;

import java.util.Arrays;
import java.util.List;

/**
 * Example 2: Demonstrating the split() functionality.
 * This example takes a message containing a list of strings, splits it into individual messages,
 * and processes each one.
 */
public class Example2_Split {
    public static void main(String[] args) {
        // 1. Prepare data: a single message containing a list of strings
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
        Msg<List<String>> initialMessage = MessageBuilder.withPayload(fruits)
                .setHeader("source", "fruit-list")
                .build();

        // 2. Define channels
        DirectChannel startChannel = new DirectChannel();
        DirectChannel outChannel = new DirectChannel();

        // 3. Define the flow
        MessageFlow.of(startChannel)
                .peek(msg -> System.out.println("Before split: " + msg.getPayload()))
                .split() // Splits the List payload into individual messages
                .peek(msg -> System.out.println("After split: " + msg.getPayload() + " (index: " + msg.getHeaders().get("splitIndex") + ")"))
                .transform(msg -> MessageBuilder.withPayload(((String) msg.getPayload()).toUpperCase())
                        .copyHeaders(msg)
                        .build())
                .bridgeTo(outChannel);

        // 4. Consume the output
        MessageFlow.of(outChannel)
                .peek(msg -> System.out.println("Final output: " + msg.getPayload()));

        // 5. Start the flow
        startChannel.send(initialMessage);
    }
}
