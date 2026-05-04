package com.physmo.channel

import com.physmo.message.Msg
import spock.lang.Specification

class QueueChannelSpec extends Specification {
    def "should enqueue and poll message maintaining FIFO order"() {
        given: "an empty QueueChannel"
          QueueChannel queueChannel = new QueueChannel()

        when: "two messages are sent to the queue"
          queueChannel.send(new Msg<String>("hello1"))
          queueChannel.send(new Msg<String>("hello2"))

        then: "polling the queue returns the messages in FIFO order"
          queueChannel.poll().get().getPayload() == "hello1"
          queueChannel.poll().get().getPayload() == "hello2"
    }
}
