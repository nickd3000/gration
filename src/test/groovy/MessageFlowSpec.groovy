import com.physmo.MessageFlow
import com.physmo.channel.DirectChannel
import com.physmo.message.Msg
import spock.lang.Specification

class MessageFlowSpec extends Specification {

    def "wssedff"() {
        given:
          DirectChannel inChannel = new DirectChannel()

        and:
          List<String> results = new ArrayList()

        and: "A message flow is created that contains a split operation"
          MessageFlow.of(inChannel)
                  .peek(m -> results.add(m.toString()))
                  .split()
                  .filter(m -> { return m.getPayload() != "two" })
                  .handler(m -> {
                      HashMap<String, String> extraHeaders = new HashMap<>()
                      extraHeaders.put("Property", "1")
                      return new Msg(m, extraHeaders)
                  })
                  .peek(m -> results.add(m.toString()))

        when:
          inChannel.send(new Msg(List.of("one", "two", "three")))

        then:
          results.size() == 3
          results.get(0) == "Msg{payload=[one, two, three], headers={}}"
          results.get(1) == "Msg{payload=one, headers={splitIndex=0, Property=1}}"
          results.get(2) == "Msg{payload=three, headers={splitIndex=2, Property=1}}"
    }
}
