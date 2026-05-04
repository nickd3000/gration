package com.physmo.processor

import com.physmo.core.MessageFlow
import com.physmo.channel.DirectChannel
import com.physmo.message.Msg
import spock.lang.Specification

class SplitSpec extends Specification {

    def "should split list message into individual messages"() {
        given: "an input channel"
          DirectChannel inChannel = new DirectChannel()

        and: "a list to store results"
          List<String> results = new ArrayList()

        and: "a message flow is created that contains a split operation"
          MessageFlow.of(inChannel)
                  .peek(m -> results.add(m.toString()))
                  .split()
                  .peek(m -> results.add(m.toString()))

        when: "a test message is sent to the input channel"
          inChannel.send(new Msg(List.of("one", "two", "three")))

        then: "four entries are found in the results list"
          results.size() == 4
          results.get(0) == "Msg{payload=[one, two, three], headers={}}"

        and: "the message data after the split contains 3 entries"
          results.get(1) == "Msg{payload=one, headers={splitIndex=0}}"
          results.get(2) == "Msg{payload=two, headers={splitIndex=1}}"
          results.get(3) == "Msg{payload=three, headers={splitIndex=2}}"
    }
}