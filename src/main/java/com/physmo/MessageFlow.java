package com.physmo;

import com.physmo.channel.DirectChannel;
import com.physmo.channel.MessageChannel;
import com.physmo.channel.SubscribableChannel;

import java.util.ArrayList;
import java.util.List;

public class MessageFlow {

    SubscribableChannel previousChannel;
    FlowComponentWrapper previousFlowComponentWrapper;

    List<FlowComponentWrapper> flowComponents = new ArrayList<>();

    public MessageFlow(DirectChannel channel) {
        previousChannel = channel;
    }

    public static MessageFlow of(DirectChannel channel) {
        return new MessageFlow(channel);
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
