package models

import play.api.libs.functional.syntax._
import play.api.libs.json._


case class Topic(name: String, partitions: List[Partition]) {
  val partitionCount: Int = partitions.size
  val replicationFactor: Int = partitions.head.replicas.size
}

object Topic {
  def apply(topic: String, partitions: List[Partition]): Topic = new Topic(topic, partitions)

  def unapply(topic: Topic): Option[(String, Int, Int, List[Partition])] = Some(topic.name, topic.partitionCount, topic.replicationFactor, topic.partitions)

  implicit val topicWrites: Writes[Topic] = (
    (__ \ "topic").write[String] and
      (__ \ "partitionCount").write[Int] and
      (__ \ "replicationFactor").write[Int] and
      (__ \ "partitions").write[List[Partition]]
    ) (unlift(Topic.unapply))
}