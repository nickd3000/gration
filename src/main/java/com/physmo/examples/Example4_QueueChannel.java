package com.physmo.examples;

import com.physmo.core.MessageFlow;
import com.physmo.channel.QueueChannel;
import com.physmo.message.MessageBuilder;
import com.physmo.poller.ManualPoller;

/**
 * Example 4: Demonstrating QueueChannel and ManualPoller.
 * This example shows how to use a QueueChannel (pollable) as a source
 * and manually trigger processing.
 */
public class Example4_QueueChannel {
    public static void main(String[] args) {
        // 1. Create a QueueChannel (Pollable)
        QueueChannel queueChannel = new QueueChannel();

        // 2. Add some messages to the queue
        queueChannel.send(MessageBuilder.withPayload("Message A").build());
        queueChannel.send(MessageBuilder.withPayload("Message B").build());
        queueChannel.send(MessageBuilder.withPayload("Message C").build());

        // 3. Setup a ManualPoller
        ManualPoller poller = new ManualPoller();

        // 4. Define the flow starting from the QueueChannel
        MessageFlow.of(queueChannel, poller)
                .peek(msg -> System.out.println("Processing from queue: " + msg.getPayload()))
                .handle(msg -> {
                    System.out.println("Handled: " + msg.getPayload());
                    return msg;
                });

        // 5. Manually trigger the poller
        System.out.println("Triggering first poll...");
        poller.triggerPollingAction();

        System.out.println("Triggering second poll...");
        poller.triggerPollingAction();

        System.out.println("Triggering third poll...");
        poller.triggerPollingAction();

        System.out.println("Triggering fourth poll (should be empty)...");
        poller.triggerPollingAction();
    }
}
