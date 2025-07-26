import com.physmo.MessageFlow
import com.physmo.channel.DirectChannel
import com.physmo.message.Msg
import spock.lang.Specification

class SplitSpec extends Specification {

    def "should split list message into individual messages"() {
        given:
          DirectChannel inChannel = new DirectChannel()

        and: "A String list to store information"
          List<String> results = new ArrayList()

        and: "A message flow is created that contains a split operation"
          MessageFlow.of(inChannel)
                  .peek(m -> results.add(m.toString()))
                  .split()
                  .peek(m -> results.add(m.toString()))

        when: "A test message is sent to the inChannel channel"
          inChannel.send(new Msg(List.of("one", "two", "three")))

        then: "4 entries are found in the results list"
          results.size() == 4
          results.get(0) == "Msg{payload=[one, two, three], headers={}}"

        and: "The message data after the split contains 3 entries"
          results.get(1) == "Msg{payload=one, headers={splitIndex=0}}"
          results.get(2) == "Msg{payload=two, headers={splitIndex=1}}"
          results.get(3) == "Msg{payload=three, headers={splitIndex=2}}"
    }
}