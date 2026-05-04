package poller

import com.physmo.core.MessageFlow
import com.physmo.message.Msg
import com.physmo.messagesource.FileMessageSource
import com.physmo.messagesource.MessageSource
import com.physmo.poller.ManualPoller
import spock.lang.Specification

class ManualPollerSpec extends Specification {

    def "ManualPoller test"() {
        given: "A custom message source is created"
          MessageSource<?> customMessageSource = new MessageSource<String>() {
              @Override
              Optional<Msg<String>> poll() {
                  return Optional.of(new Msg<String>("Hello"))
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
