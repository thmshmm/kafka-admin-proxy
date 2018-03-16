import java.io.File

import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play._
import play.api.{Configuration, Logger}
import services.KafkaService


class KafkaServiceTest extends PlaySpec with BeforeAndAfterAll {
  var kafkaService: KafkaService = null

  override def beforeAll(): Unit = {
    val testConfig = ConfigFactory.parseFile(new File("conf/application.test.conf"))
    kafkaService = new KafkaService(new Configuration(testConfig))
    Logger.info(kafkaService.waitUntilConnected.toString)
  }

  "The KafkaService" must {
    "create a Topic" in {
      kafkaService.createTopic("test-create-topic-1992817", 1, 1).isSuccess mustBe true
    }
    "delete the created topic 'test1'" in {
      kafkaService.deleteTopic("test-create-topic-1992817").isSuccess mustBe true
    }
  }
}