package com.physmo;

import com.physmo.channel.DirectChannel;
import com.physmo.channel.MessageChannel;
import com.physmo.channel.PollableChannel;
import com.physmo.channel.SubscribableChannel;
import com.physmo.message.Msg;
import com.physmo.messageSource.MessageSource;
import com.physmo.poller.Poller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageFlow {

    SubscribableChannel previousChannel;
    FlowComponentWrapper previousFlowComponentWrapper;

    List<FlowComponentWrapper> flowComponents = new ArrayList<>();

    public MessageFlow(SubscribableChannel channel) {
        previousChannel = channel;
    }

    public static MessageFlow of(SubscribableChannel channel) {
        return new MessageFlow(channel);
    }

    public static MessageFlow of(PollableChannel channel, Poller poller) {
        DirectChannel channelConnector = new DirectChannel();

        poller.setPollingAction(() -> {
            Optional<Msg<?>> polledMessage = channel.poll();
            polledMessage.ifPresent(channelConnector::send);
        });

        return new MessageFlow(channelConnector);
    }

    public static MessageFlow of(MessageSource messageSource, Poller poller) {
        DirectChannel channelConnector = new DirectChannel();

        poller.setPollingAction(() -> {
            Optional<Msg<?>> polledMessage = messageSource.poll();
            polledMessage.ifPresent(channelConnector::send);
        });

        return new MessageFlow(channelConnector);
    }

    public MessageFlow handler(Handler handler) {
        addFlowComponent(handler);
        return this;
    }

    private void addFlowComponent(FlowComponent flowComponent) {

        DirectChannel componentOutputChannel = new DirectChannel();

        FlowComponentWrapper wrapper = new FlowComponentWrapper();
        wrapper.setFlowComponent(flowComponent);
        wrapper.setOutputChannel(componentOutputChannel);
        previousChannel.addSubscriber(wrapper);

        flowComponents.add(wrapper);
        previousChannel = componentOutputChannel;
        previousFlowComponentWrapper = wrapper;
    }

    public MessageFlow peek(Peek peek) {
        addFlowComponent(peek);
        return this;
    }

    public MessageFlow filter(Filter filter) {
        addFlowComponent(filter);
        return this;
    }

    public MessageFlow split() {
        addFlowComponent(new Split());
        return this;
    }

    public void channel(MessageChannel channel) {
        previousFlowComponentWrapper.setOutputChannel(channel);
    }
}
