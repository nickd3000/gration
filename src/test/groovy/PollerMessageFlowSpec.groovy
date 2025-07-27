import com.physmo.MessageFlow
import com.physmo.channel.DirectChannel
import com.physmo.channel.PollableChannel
import com.physmo.message.Msg
import com.physmo.poller.FixedRatePoller
import spock.lang.Specification

class PollerMessageFlowSpec extends Specification {

    def "PollableChannel and Poller work in combination"() {
        given: "Parameters are defined"
          def pollingIntervalMs = 100
          def testDurationMs = 1000
          def minimumExpectedMessages = 9
          def expectedMessageContent = "Msg{payload=hello there, headers={}}"
          def testMessage = new Msg("hello there")

        and: "A PollableChannel is created that emits a string"
          PollableChannel constantMessageChannel = new PollableChannel() {
              @Override
              Optional<Msg<?>> poll() {
                  return Optional.of(testMessage)
              }
          }

        and: "A result list is created"
          List<String> results = new ArrayList()

        and: "A message flow is created"
          MessageFlow.of(constantMessageChannel, new FixedRatePoller(pollingIntervalMs))
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
