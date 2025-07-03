package com.physmo;

import com.physmo.channel.DirectChannel;

import java.util.Map;

public class FlowContext {
    Map<String, MessageFlow> flows;
    Map<String, DirectChannel> channels;
}
