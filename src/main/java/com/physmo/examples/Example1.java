package com.physmo.examples;


import com.physmo.core.MessageFlow;
import com.physmo.channel.DirectChannel;
import com.physmo.message.MessageBuilder;
import com.physmo.message.Msg;

import java.util.Arrays;
import java.util.List;

/**
 * Example 1: Basic Message Flow demonstration.
 * This example shows a simple flow that enriches headers, filters messages,
 * and handles them before bridging to another channel.
 */
public class Example1 {
    public static void main(String[] args) {

        // 1. Prepare data: a list of messages
        List<Msg<?>> msgList = Arrays.asList(
                MessageBuilder.withPayload("1").build(),
                MessageBuilder.withPayload("2").build(),
                MessageBuilder.withPayload("3").build());

        // 2. Define channels
        DirectChannel startChannel = new DirectChannel();
        DirectChannel outChannel = new DirectChannel();

        // 3. Define the main flow
        MessageFlow.of(startChannel)
                .transform(Example1::enrichHeaders)
                .peek(msg -> System.out.println("peek " + msg.getPayload()))
                .filter(Example1::filter1)
                .handle(Example1::handler1)
                .bridgeTo(outChannel);

        // 4. Define a second flow to consume from the output channel
        MessageFlow.of(outChannel)
                .peek(msg -> System.out.println("second flow " + msg.getPayload()));

        // 5. Start the flow by sending the messages
        startChannel.send(msgList);

    }

    public static Msg<?> enrichHeaders(Msg<?> msg) {
        return MessageBuilder.fromMessage(msg)
                .setHeader("enrich", "enriched by enrichHeaders")
                .build();
    }

    public static boolean filter1(Msg<?> msg) {
        return msg.getPayload().equals("2");
    }

    public static Msg<?> handler1(Msg<?> msg) {
        System.out.println(msg);
        return msg;
    }
}