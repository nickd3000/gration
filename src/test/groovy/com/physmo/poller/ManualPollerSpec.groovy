package com.physmo.poller

import com.physmo.core.MessageFlow
import com.physmo.message.Msg
import com.physmo.messagesource.MessageSource
import spock.lang.Specification

class ManualPollerSpec extends Specification {
    def "should process messages when manual poller is triggered"() {
        given: "a custom message source and a manual poller"
          MessageSource<String> customMessageSource = new MessageSource<String>() {
              @Override
              Optional<Msg<String>> poll() {
                  return Optional.of(new Msg<String>("Hello"))
              }
          }
          ManualPoller manualPoller = new ManualPoller()

        and: "a result list and a message flow"
          List<String> results = new ArrayList()
          MessageFlow.of(customMessageSource, manualPoller)
                  .peek(m -> results.add(m.toString()))

        when: "the manual poller is triggered three times"
          manualPoller.poll()
          manualPoller.poll()
          manualPoller.poll()

        then: "the results list contains three messages"
          results.size() == 3
    }
}
