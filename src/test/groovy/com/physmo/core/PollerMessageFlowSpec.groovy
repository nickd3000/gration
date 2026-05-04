package com.physmo.core

import com.physmo.channel.PollableChannel
import com.physmo.message.Msg
import com.physmo.poller.FixedRatePoller
import spock.lang.Specification

class PollerMessageFlowSpec extends Specification {
    def "should process messages when PollableChannel and Poller work in combination"() {
        given: "parameters and a custom PollableChannel"
          def pollingIntervalMs = 100
          def testDurationMs = 1000
          def minimumExpectedMessages = 9
          def expectedMessageContent = "Msg{payload=hello there, headers={}}"
          def testMessage = new Msg("hello there")
          PollableChannel constantMessageChannel = new PollableChannel() {
              @Override
              Optional<Msg<?>> poll() {
                  return Optional.of(testMessage)
              }
          }

        and: "a result list and a message flow"
          List<String> results = new ArrayList()
          FixedRatePoller poller = new FixedRatePoller(pollingIntervalMs)
          MessageFlow.of(constantMessageChannel, poller)
                  .peek(m -> results.add(m.toString()))

        when: "the poller is initialized and execution sleeps for a test duration"
          poller.init()
          sleep(testDurationMs)
          poller.stop()

        then: "the results list contains the expected number of messages"
          results.size() >= minimumExpectedMessages
          results.get(0) == expectedMessageContent
          results.get(1) == expectedMessageContent
    }
}
