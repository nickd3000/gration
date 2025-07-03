import com.physmo.MessageFlow
import com.physmo.channel.DirectChannel
import com.physmo.message.Msg
import spock.lang.Specification

class SplitSpec extends Specification {

    def "wedff"() {
        given:
          DirectChannel inChannel = new DirectChannel()

        and: "A message flow is created that contains a split operation"
          MessageFlow.of(inChannel).peek(m -> println m).split().peek(m -> println m)

        when:
          inChannel.send(new Msg(List.of("one", "two", "three")))

        then:
          1 == 1
    }
}
