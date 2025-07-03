import com.physmo.channel.DirectChannel
import com.physmo.message.Msg
import com.physmo.message.Subscriber
import spock.lang.Specification

class DirectChannelSpec extends Specification {

    def "should send message to subscriber"() {
        given:
          def channel = new DirectChannel()
          Msg<String> received = null
          Subscriber subscriber = new Subscriber() {
              @Override
              void receive(Msg<?> msg) {
                  received = msg as Msg<String>
              }
          }
          channel.addSubscriber(subscriber)

        when:
          channel.send(new Msg<String>("hello"))

        then:
          received.getPayload() == "hello"
    }
}