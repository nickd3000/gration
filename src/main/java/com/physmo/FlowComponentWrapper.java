package com.physmo;

import com.physmo.channel.MessageChannel;
import com.physmo.message.Msg;
import com.physmo.message.Subscriber;

import java.util.List;

public class FlowComponentWrapper implements Subscriber {
    FlowComponent flowComponent;

    MessageChannel outputChannel;


    @Override
    public void receive(Msg<?> msg) {
        // TODO: can each component handle this stuff?
        if (outputChannel == null) {
            System.out.println("No output channel set");
            return;
        }
        if (flowComponent instanceof Handler handler) {
            outputChannel.send(handler.handle(msg));
        }
        if (flowComponent instanceof Filter filter) {
            if (filter.filter(msg)) {
                outputChannel.send(msg);
            }
        }
        if (flowComponent instanceof Peek peek) {
            peek.peek(msg);
            outputChannel.send(msg);
        }
        if (flowComponent instanceof Split split) {
            List<Msg<?>>messages =  split.handle(msg);

            for (Msg<?> message : messages) {
                outputChannel.send(message);
            }
        }
    }

    public void setOutputChannel(MessageChannel outputChannel) {
        this.outputChannel = outputChannel;
    }

    public void setFlowComponent(FlowComponent flowComponent) {
        this.flowComponent = flowComponent;
    }

}