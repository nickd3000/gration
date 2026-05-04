package com.physmo.examples;

import com.physmo.core.MessageFlow;
import com.physmo.messagesource.FileMessageSource;
import com.physmo.poller.FixedRatePoller;

import java.io.File;

/**
 * Example 3: Demonstrating FileMessageSource and FixedRatePoller.
 * This example polls the current directory for files every 2 seconds,
 * splits the list of files, and prints their names.
 */
public class Example3_FilePoller {
    public static void main(String[] args) throws InterruptedException {
        // 1. Setup the message source (polls the current directory)
        FileMessageSource fileSource = new FileMessageSource(".");

        // 2. Setup a poller that runs every 2 seconds
        FixedRatePoller poller = new FixedRatePoller(2000);

        // 3. Define the flow
        MessageFlow.of(fileSource, poller)
                .split() // FileMessageSource returns Msg<List<File>>, so we split it
                .peek(msg -> {
                    File file = (File) msg.getPayload();
                    System.out.println("Found file: " + file.getName());
                })
                .handle(msg -> {
                    // Just a dummy handler to show processing
                    return msg;
                });

        poller.init();

        System.out.println("Polling started. Press Ctrl+C to stop, or wait 10 seconds...");

        // Keep the application running for a bit to see the poller in action
        Thread.sleep(10000);

        poller.stop();
        System.out.println("Polling stopped.");
    }
}
