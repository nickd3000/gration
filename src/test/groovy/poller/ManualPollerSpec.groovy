package poller

import com.physmo.MessageFlow
import com.physmo.message.Msg
import com.physmo.messageSource.FileMessageSource
import com.physmo.messageSource.MessageSource
import com.physmo.poller.ManualPoller
import spock.lang.Specification

class ManualPollerSpec extends Specification {

    def "DirectoryMessageSource test"() {
        given: "A custom message source is created"
          MessageSource customMessageSource = new MessageSource() {
              @Override
              Optional<Msg> poll() {
                  return Optional.of(new Msg("Hello"))
              }
          }

        and: "A String list is created to store results"
          List<String> results = new ArrayList()

        and: "A manual poller is created"
          ManualPoller manualPoller = new ManualPoller()

        and: "A message flow is created"
          MessageFlow.of(customMessageSource, manualPoller)
                  .peek(m -> results.add(m.toString()))

        and: "The manual poller is triggered 3 times"
          manualPoller.poll()
          manualPoller.poll()
          manualPoller.poll()
          println(results)

        expect:
          results.size() == 3

    }
}
