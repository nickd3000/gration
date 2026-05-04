package com.physmo.core;

import com.physmo.core.MessageFlow;

import com.physmo.channel.DirectChannel;

import java.util.Map;

public class FlowContext {
    Map<String, MessageFlow> flows;
    Map<String, DirectChannel> channels;
}
