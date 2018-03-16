package models

import play.api.libs.functional.syntax._
import play.api.libs.json._


case class Partition(id: Int, leader: Int, replicas: List[Int], inSyncReplicas: List[Int])

object Partition {
  def apply(id: Int, leader: Int, replicas: List[Int], inSyncReplicas: List[Int]): Partition = new Partition(id, leader, replicas, inSyncReplicas)

  def unapply(partition: Partition): Option[(Int, Int, List[Int], List[Int])] = Some(partition.id, partition.leader, partition.replicas, partition.inSyncReplicas)

  implicit val partitionWrites: Writes[Partition] = (
    (__ \ "partition").write[Int] and
      (__ \ "leader").write[Int] and
      (__ \ "replicas").write[List[Int]] and
      (__ \ "inSyncReplicas").write[List[Int]]
    ) (unlift(Partition.unapply))
}