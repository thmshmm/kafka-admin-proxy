package controllers

import javax.inject.Inject

import io.swagger.annotations._
import models.NewTopic
import org.apache.kafka.common.errors.{TopicExistsException, UnknownTopicOrPartitionException}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import play.api.mvc._
import services.KafkaService
import utils.JsonResponse

import scala.util.{Failure, Success}


@Api(value = "/topics")
class Topics @Inject()(cc: ControllerComponents, kafkaService: KafkaService) extends AbstractController(cc) {

  import Topics._

  val logger = LoggerFactory.getLogger(this.getClass)

  def getAll = Action {
    Ok(Json.toJson(kafkaService.getTopics))
  }

  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "Topic created."),
    new ApiResponse(code = 200, message = "Topic already exists.")
  ))
  def create(topic: String) = Action { request =>
    val topicData = Json.fromJson[NewTopic](request.body.asJson.get).get

    kafkaService.createTopic(topicData.name, topicData.partitions, topicData.replicationFactor) match {
      case Success(success: Unit) => Created(TOPIC_CREATED_RESP.toString)
      case Failure(exception: TopicExistsException) => Ok(TOPIC_EXISTS_RESP.toString)
    }
  }

  @ApiOperation(
    value = "deleteTopic",
    notes = "Deletes a specific topic.",
    httpMethod = "DELETE",
    produces = "application/json"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Topic does not exist."),
    new ApiResponse(code = 202, message = "Topic is marked for deletion.")
  ))
  def delete(topic: String) = Action { request =>
    kafkaService.deleteTopic(topic) match {
      case Success(success: Unit) => Accepted(TOPIC_MARKED_FOR_DELETION_RESP.toString)
      case Failure(exception: UnknownTopicOrPartitionException) => NotFound(TOPIC_NOT_FOUND_RESP.toString)
    }
  }
}

object Topics {
  val TOPIC_CREATED_RESP = JsonResponse(201, "Topic created.")
  val TOPIC_EXISTS_RESP = JsonResponse(200, "Topic already exists.")
  val TOPIC_MARKED_FOR_DELETION_RESP = JsonResponse(202, "Topic is marked for deletion. Note: This will have no impact if delete.topic.enable is not set to true.")
  val TOPIC_NOT_FOUND_RESP = JsonResponse(404, "Topic does not exist.")
}