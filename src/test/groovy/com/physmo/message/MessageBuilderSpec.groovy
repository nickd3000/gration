package com.physmo.message

import spock.lang.Specification

class MessageBuilderSpec extends Specification {
    def "withPayload should create builder with specified payload"() {
        given:
        def payload = "test payload"

        when:
        def builder = MessageBuilder.withPayload(payload)
        def message = builder.build()

        then:
        message.getPayload() == payload
        message.getHeaders().isEmpty()
    }

    def "setHeader should add header to message"() {
        given:
        def builder = MessageBuilder.withPayload("test")

        when:
        builder.setHeader("key1", "value1")
        builder.setHeader("key2", 123)
        def message = builder.build()

        then:
        message.getHeaders().size() == 2
        message.getHeaders().get("key1") == "value1"
        message.getHeaders().get("key2") == 123
    }

    def "removeHeader should remove header from message"() {
        given:
        def builder = MessageBuilder.withPayload("test")
                .setHeader("key1", "value1")
                .setHeader("key2", "value2")

        when:
        builder.removeHeader("key1")
        def message = builder.build()

        then:
        message.getHeaders().size() == 1
        !message.getHeaders().containsKey("key1")
        message.getHeaders().get("key2") == "value2"
    }

    def "copyHeaders from map should add all headers"() {
        given:
        def headers = [key1: "value1", key2: "value2"]
        def builder = MessageBuilder.withPayload("test")

        when:
        builder.copyHeaders(headers)
        def message = builder.build()

        then:
        message.getHeaders().size() == 2
        message.getHeaders().get("key1") == "value1"
        message.getHeaders().get("key2") == "value2"
    }

    def "copyHeaders from map should handle null map"() {
        given:
        def builder = MessageBuilder.withPayload("test").setHeader("existing", "val")

        when:
        builder.copyHeaders((Map) null)
        def message = builder.build()

        then:
        message.getHeaders().size() == 1
        message.getHeaders().get("existing") == "val"
    }

    def "copyHeaders from message should add all headers"() {
        given:
        def originalHeaders = [key1: "value1"]
        def originalMessage = new Msg("original", originalHeaders)
        def builder = MessageBuilder.withPayload("new")

        when:
        builder.copyHeaders(originalMessage)
        def message = builder.build()

        then:
        message.getHeaders().size() == 1
        message.getHeaders().get("key1") == "value1"
        message.getPayload() == "new"
    }

    def "copyHeaders from message should handle null message"() {
        given:
        def builder = MessageBuilder.withPayload("test").setHeader("existing", "val")

        when:
        builder.copyHeaders((Msg) null)
        def message = builder.build()

        then:
        message.getHeaders().size() == 1
        message.getHeaders().get("existing") == "val"
    }

    def "fromMessage should create builder from existing message"() {
        given:
        def originalHeaders = [key1: "value1"]
        def originalMessage = new Msg("original", originalHeaders)

        when:
        def builder = MessageBuilder.fromMessage(originalMessage)
        def newMessage = builder.build()

        then:
        newMessage.getPayload() == "original"
        newMessage.getHeaders() == originalHeaders
    }

    def "fluent API should allow chaining"() {
        when:
        def message = MessageBuilder.withPayload("chained")
                .setHeader("h1", "v1")
                .copyHeaders([h2: "v2"])
                .removeHeader("h1")
                .build()

        then:
        message.getPayload() == "chained"
        message.getHeaders().size() == 1
        message.getHeaders().get("h2") == "v2"
        !message.getHeaders().containsKey("h1")
    }
}
