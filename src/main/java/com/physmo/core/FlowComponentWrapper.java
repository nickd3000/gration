package com.physmo.core;

import com.physmo.processor.Processor;

import com.physmo.channel.MessageChannel;
import com.physmo.message.Msg;
import com.physmo.message.Subscriber;

import java.util.List;

public class FlowComponentWrapper implements Subscriber {
    Processor processor;

    MessageChannel outputChannel;


    @Override
    public void receive(Msg<?> msg) {
        if (outputChannel == null) {
            System.out.println("No output channel set");
            return;
        }

        List<Msg<?>> messages = processor.process(msg);

        for (Msg<?> message : messages) {
            outputChannel.send(message);
        }
    }

    public void setOutputChannel(MessageChannel outputChannel) {
        this.outputChannel = outputChannel;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

}