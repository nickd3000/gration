package messageSource

import com.physmo.MessageFlow
import com.physmo.channel.PollableChannel
import com.physmo.message.Msg
import com.physmo.messageSource.DirectoryMessageSource
import com.physmo.poller.FixedRatePoller
import spock.lang.Specification

class DirectoryMessageSourceSpec extends Specification {

    def "DirectoryMessageSource test"() {
        given: "Parameters are defined"
          def pollingIntervalMs = 600
          def testDurationMs = 1000
          def minimumExpectedMessages = 9
          def expectedMessageContent = "Msg{payload=hello there, headers={}}"
          String path = "/tmp"


        and: "A result list is created"
          List<String> results = new ArrayList()

        and: "A message flow is created"
          MessageFlow.of(new DirectoryMessageSource(path), new FixedRatePoller(pollingIntervalMs))
                  .peek(m -> results.add(m.toString()))

        and: "Execution sleeps for a short time"
          sleep(testDurationMs)
          println(results)

        expect:
          results.size() >= minimumExpectedMessages
          results.get(0) == expectedMessageContent
          results.get(1) == expectedMessageContent
    }
}
