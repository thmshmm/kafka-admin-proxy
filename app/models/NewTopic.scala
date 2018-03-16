package models

import play.api.libs.functional.syntax._
import play.api.libs.json._


case class NewTopic(name: String, partitions: Int, replicationFactor: Int)

object NewTopic {
  def apply(name: String, partitions: Int, replicationFactor: Int): NewTopic = new NewTopic(name, partitions, replicationFactor)

  def unapply(topic: NewTopic): Option[(String, Int, Int)] = Some((topic.name, topic.partitions, topic.replicationFactor))

  implicit val topicReads: Reads[NewTopic] = (
    (__ \ "topic").read[String] and
      (__ \ "partitions").read[Int] and
      (__ \ "replicationFactor").read[Int]
    ) (NewTopic.apply _)
}
