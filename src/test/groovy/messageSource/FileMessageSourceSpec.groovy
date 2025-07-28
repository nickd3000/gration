package messageSource

import com.physmo.MessageFlow
import com.physmo.messageSource.FileMessageSource
import com.physmo.poller.FixedRatePoller
import spock.lang.Specification

class FileMessageSourceSpec extends Specification {

    def "DirectoryMessageSource test"() {
        given: "Parameters are defined"
          def pollingIntervalMs = 600
          def testDurationMs = 1000
          String path = "/tmp"

        and: "A result list is created"
          List<String> results = new ArrayList()

        and: "A message flow is created"
          MessageFlow.of(new FileMessageSource(path), new FixedRatePoller(pollingIntervalMs))
                  .peek(m -> results.add(m.toString()))
                  .split()
                  .handler { m -> println m.toString() }

        and: "Execution sleeps for a short time"
          sleep(testDurationMs)
          println(results)

        expect:
          results.size() == 1

    }
}
