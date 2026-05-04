package com.physmo.examples;

import com.physmo.core.MessageFlow;
import com.physmo.messagesource.HttpMessageSource;
import com.physmo.poller.FixedRatePoller;

/**
 * Example 5: Demonstrating HttpMessageSource and FixedRatePoller.
 * This example polls a public time API every 5 seconds and prints the response.
 */
public class Example5_HttpPoller {
    public static void main(String[] args) throws InterruptedException {
        // 1. Setup the message source (polls a public time API)
        // Using timeapi.io to get current UTC time
        String url = "https://timeapi.io/api/Time/current/zone?timeZone=UTC";
        HttpMessageSource httpSource = new HttpMessageSource(url);

        // 2. Setup a poller that runs every 5 seconds
        FixedRatePoller poller = new FixedRatePoller(5000);

        // 3. Define the flow
        MessageFlow.of(httpSource, poller)
                .peek(msg -> {
                    System.out.println("Received HTTP response:");
                    System.out.println("Status Code: " + msg.getHeaders().get("http_status_code"));
                    System.out.println("Payload: " + msg.getPayload());
                    System.out.println("--------------------------------------------------");
                })
                .handle(msg -> {
                    // Just a dummy handler to show processing
                    return msg;
                });

        poller.init();

        System.out.println("HTTP Polling started for: " + url);
        System.out.println("Press Ctrl+C to stop, or wait 20 seconds...");

        // Keep the application running for a bit to see the poller in action
        Thread.sleep(20000);

        poller.stop();
        System.out.println("Polling stopped.");
    }
}
