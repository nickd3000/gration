package com.physmo.messagesource

import com.physmo.core.MessageFlow
import com.physmo.poller.ManualPoller
import spock.lang.Specification

class FileMessageSourceSpec extends Specification {
    def "should list files using FileMessageSource and ManualPoller"() {
        given: "a temporary path and a manual poller"
          String path = "/tmp"
          ManualPoller manualPoller = new ManualPoller()

        and: "a result list and a message flow"
          List<String> results = new ArrayList()
          MessageFlow.of(new FileMessageSource(path), manualPoller)
                  .peek(m -> results.add(m.toString()))
                  .split()

        when: "the manual poller is triggered"
          manualPoller.poll()

        then: "the results list contains one message representing the list of files"
          results.size() == 1
    }
}
