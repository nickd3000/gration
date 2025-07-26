package com.physmo.reference;


import com.physmo.MessageFlow;
import com.physmo.channel.DirectChannel;
import com.physmo.message.Msg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Example1 {
    public static void main(String[] args) {

        List<Msg<?>> msgList = Arrays.asList(new Msg<>("1"), new Msg<>("2"), new Msg<>("3"));

        DirectChannel startChannel = new DirectChannel();
        DirectChannel outChannel = new DirectChannel();

        MessageFlow.of(startChannel)
                .handler(Example1::enrichHeaders)
                .peek(msg -> System.out.println("peek " + msg.getPayload()))
                .filter(Example1::filter1)
                .handler(Example1::handler1)
                .channel(outChannel);

        MessageFlow.of(outChannel)
                .peek(msg -> System.out.println("second flow " + msg.getPayload()));

        startChannel.send(msgList);

    }

    public static Msg<?> enrichHeaders(Msg<?> msg) {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("enrich", "enriched by enrichHeaders");
        return new Msg<>(msg.getPayload(), headers);
    }

    public static boolean filter1(Msg<?> msg) {
        return msg.getPayload().equals("2");
    }

    public static Msg<?> handler1(Msg<?> msg) {
        System.out.println(msg);
        return msg;
    }
}