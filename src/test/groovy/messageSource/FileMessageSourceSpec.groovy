package messageSource

import com.physmo.MessageFlow
import com.physmo.messageSource.FileMessageSource
import com.physmo.poller.FixedRatePoller
import com.physmo.poller.ManualPoller
import com.physmo.poller.Poller
import spock.lang.Specification

class FileMessageSourceSpec extends Specification {

    def "DirectoryMessageSource test"() {
        given: "Parameters are defined"
          String path = "/tmp"

        and: "A String list is created to store results"
          List<String> results = new ArrayList()

        and: "A manual poller is created"
          ManualPoller manualPoller = new ManualPoller()

        and: "A message flow is created"
          MessageFlow.of(new FileMessageSource(path), manualPoller)
                  .peek(m -> results.add(m.toString()))
                  .split()
                  .handler { m -> println m.toString() }

        and: "The manual poller is triggered"
          manualPoller.poll()
          println(results)

        expect:
          results.size() == 1

    }
}
