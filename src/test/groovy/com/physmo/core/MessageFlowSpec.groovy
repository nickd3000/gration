package com.physmo.core

import com.physmo.channel.DirectChannel
import com.physmo.message.Msg
import spock.lang.Specification

class MessageFlowSpec extends Specification {
    def "should process messages through a flow with split, filter and handle operations"() {
        given: "an input channel"
          DirectChannel inChannel = new DirectChannel()

        and: "a list to store results"
          List<String> results = new ArrayList()

        and: "a message flow is created with split, filter and handle operations"
          MessageFlow.of(inChannel)
                  .peek(m -> results.add(m.toString()))
                  .split()
                  .filter(m -> m.getPayload() != "two")
                  .handle(m -> {
                      HashMap<String, String> extraHeaders = new HashMap<>()
                      extraHeaders.put("Property", "1")
                      return new Msg(m, extraHeaders)
                  })
                  .peek(m -> results.add(m.toString()))

        when: "a test message containing a list is sent to the input channel"
          inChannel.send(new Msg(List.of("one", "two", "three")))

        then: "the results list contains the expected processed messages"
          results.size() == 3
          results.get(0) == "Msg{payload=[one, two, three], headers={}}"
          results.get(1) == "Msg{payload=one, headers={splitIndex=0, Property=1}}"
          results.get(2) == "Msg{payload=three, headers={splitIndex=2, Property=1}}"
    }
}
