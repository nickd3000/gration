package com.physmo.channel

import com.physmo.message.Msg
import com.physmo.message.Subscriber
import spock.lang.Specification

class DirectChannelSpec extends Specification {
    def "should send message to subscriber"() {
        given: "a DirectChannel and a subscriber"
          def channel = new DirectChannel()
          Msg<String> received = null
          Subscriber subscriber = new Subscriber() {
              @Override
              void receive(Msg<?> msg) {
                  received = msg as Msg<String>
              }
          }
          channel.subscribe(subscriber)

        when: "a message is sent to the channel"
          channel.send(new Msg<String>("hello"))

        then: "the subscriber receives the message"
          received.getPayload() == "hello"

        when: "the subscriber is unsubscribed and another message is sent"
          received = null
          channel.unsubscribe(subscriber)
          channel.send(new Msg<String>("world"))

        then: "the subscriber does not receive the message"
          received == null
    }
}