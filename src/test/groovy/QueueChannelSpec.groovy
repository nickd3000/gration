import com.physmo.channel.QueueChannel
import com.physmo.message.Msg
import spock.lang.Specification


class QueueChannelSpec extends Specification {
    def "should enqueue and poll message maintaining FIFO order"() {
        given: "an empty QueueChannel"
          QueueChannel queueChannel = new QueueChannel()

         when: "a message with payload 'hello' is sent"
          queueChannel.send(new Msg<String>("hello1"))
          queueChannel.send(new Msg<String>("hello2"))

        then: "polling the queue returns the sent message with correct payload"
          queueChannel.poll().get().getPayload() == "hello1"
          queueChannel.poll().get().getPayload() == "hello2"
    }

}
