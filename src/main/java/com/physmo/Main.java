package com.physmo;


import com.physmo.channel.DirectChannel;
import com.physmo.message.MessageBuilder;
import com.physmo.message.Msg;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Msg<?>> msgList = Arrays.asList(
                MessageBuilder.withPayload("1").build(),
                MessageBuilder.withPayload("2").build(),
                MessageBuilder.withPayload("3").build());

        DirectChannel startChannel = new DirectChannel();
        DirectChannel outChannel = new DirectChannel();

        MessageFlow.of(startChannel)
                .transform(Main::enrichHeaders)
                .peek(msg -> System.out.println("peek " + msg.getPayload()))
                .filter(Main::filter1)
                .handle(Main::handler1)
                .bridgeTo(outChannel);

        MessageFlow.of(outChannel)
                .peek(msg -> System.out.println("second flow " + msg.getPayload()));

        startChannel.send(msgList);

    }

    public static Msg<?> enrichHeaders(Msg<?> msg) {
        return MessageBuilder.fromMessage(msg)
                .setHeader("enrich", "a")
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