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

/**
 * MessageFlow is a builder-style API for creating message processing pipelines.
 * It allows chaining various processing components like handlers, transformers, filters, and splitters
 * to define how messages should be processed as they flow through the system.
 */
public class MessageFlow {

    SubscribableChannel previousChannel;
    FlowComponentWrapper previousFlowComponentWrapper;

    List<FlowComponentWrapper> flowComponents = new ArrayList<>();

    /**
     * Constructs a new MessageFlow starting with the specified subscribable channel.
     *
     * @param channel the starting channel for the message flow
     */
    public MessageFlow(SubscribableChannel channel) {
        previousChannel = channel;
    }

    /**
     * Creates a new MessageFlow starting from the specified subscribable channel.
     *
     * @param channel the starting channel for the message flow
     * @return a new MessageFlow instance
     */
    public static MessageFlow of(SubscribableChannel channel) {
        return new MessageFlow(channel);
    }

    /**
     * Creates a new MessageFlow that polls messages from a pollable channel using the provided poller.
     * The polled messages are sent to an internal channel that starts the flow.
     *
     * @param channel the pollable channel to poll messages from
     * @param poller the poller that triggers the polling action
     * @return a new MessageFlow instance
     */
    public static MessageFlow of(PollableChannel channel, Poller poller) {
        DirectChannel channelConnector = new DirectChannel();

        poller.setPollingAction(() -> {
            Optional<Msg<?>> polledMessage = channel.poll();
            polledMessage.ifPresent(channelConnector::send);
        });

        return new MessageFlow(channelConnector);
    }

    /**
     * Creates a new MessageFlow that polls messages from a message source using the provided poller.
     * The polled messages are sent to an internal channel that starts the flow.
     *
     * @param messageSource the source of messages
     * @param poller the poller that triggers the polling action
     * @return a new MessageFlow instance
     */
    public static MessageFlow of(MessageSource<?> messageSource, Poller poller) {
        DirectChannel channelConnector = new DirectChannel();

        poller.setPollingAction(() -> {
            messageSource.poll().ifPresent(channelConnector::send);
        });

        return new MessageFlow(channelConnector);
    }

    /**
     * Adds a generic message handler to the flow.
     *
     * @param handler the handler to process messages
     * @return this MessageFlow instance for chaining
     */
    public MessageFlow handle(MessageHandler handler) {
        addFlowComponent(Processor.fromHandler(handler));
        return this;
    }

    /**
     * Adds a transformer to the flow for modifying message payloads or headers.
     *
     * @param transformer the transformer to apply to messages
     * @return this MessageFlow instance for chaining
     */
    public MessageFlow transform(Transformer transformer) {
        addFlowComponent(Processor.fromTransformer(transformer));
        return this;
    }

    /**
     * Connects the end of this flow to the specified message channel.
     * This is a terminal operation that bridges the flow to an output channel.
     *
     * @param channel the output channel to bridge to
     * @return this MessageFlow instance for chaining
     */
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

    /**
     * Adds a peek component to the flow, allowing for side effects without modifying the message.
     *
     * @param peek the peek action to perform on messages
     * @return this MessageFlow instance for chaining
     */
    public MessageFlow peek(Peek peek) {
        addFlowComponent(Processor.fromPeek(peek));
        return this;
    }

    /**
     * Adds a filter to the flow. Only messages that satisfy the filter predicate will continue.
     *
     * @param filter the filter to apply to messages
     * @return this MessageFlow instance for chaining
     */
    public MessageFlow filter(Filter filter) {
        addFlowComponent(Processor.fromFilter(filter));
        return this;
    }

    /**
     * Adds a split component to the flow. If the message payload is a List, it will be split
     * into individual messages, each containing one element of the list.
     *
     * @return this MessageFlow instance for chaining
     */
    public MessageFlow split() {
        addFlowComponent(Processor.fromSplit(new Split()));
        return this;
    }

    /**
     * Sets the output channel for the last component in the flow.
     * Similar to bridgeTo, but typically used at the end of the chain.
     *
     * @param channel the output channel to connect to
     */
    public void channel(MessageChannel channel) {
        previousFlowComponentWrapper.setOutputChannel(channel);
    }
}
