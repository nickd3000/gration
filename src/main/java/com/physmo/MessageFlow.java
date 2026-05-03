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

    public MessageFlow handle(MessageHandler handler) {
        addFlowComponent(Processor.fromHandler(handler));
        return this;
    }

    public MessageFlow transform(Transformer transformer) {
        addFlowComponent(Processor.fromTransformer(transformer));
        return this;
    }

    public MessageFlow bridgeTo(MessageChannel channel) {
        previousFlowComponentWrapper.setOutputChannel(channel);
        return this;
    }

    /**
     * Adds a flow component to the message flow by wrapping the given processor
     * into a {@code FlowComponentWrapper}, setting its output channel, and subscribing
     * the wrapper to the previous channel in the flow. Updates the flow tracking properties
     * to reflect the newly added component.
     *
     * @param processor the processor to be wrapped and added as a flow component. The processor
     *                  defines the processing logic for messages passing through this component.
     */
    private void addFlowComponent(Processor processor) {

        DirectChannel componentOutputChannel = new DirectChannel();

        FlowComponentWrapper wrapper = new FlowComponentWrapper();
        wrapper.setProcessor(processor);
        wrapper.setOutputChannel(componentOutputChannel);
        previousChannel.addSubscriber(wrapper);

        flowComponents.add(wrapper);
        previousChannel = componentOutputChannel;
        previousFlowComponentWrapper = wrapper;
    }

    public MessageFlow peek(Peek peek) {
        addFlowComponent(Processor.fromPeek(peek));
        return this;
    }

    public MessageFlow filter(Filter filter) {
        addFlowComponent(Processor.fromFilter(filter));
        return this;
    }

    public MessageFlow split() {
        addFlowComponent(Processor.fromSplit(new Split()));
        return this;
    }

    public void channel(MessageChannel channel) {
        previousFlowComponentWrapper.setOutputChannel(channel);
    }
}
