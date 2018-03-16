package services

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}

import kafka.admin.AdminUtils
import kafka.utils.ZkUtils
import models.{Partition, Topic}
import play.api.Configuration

import scala.util.Try


@Singleton
class KafkaService @Inject()(config: Configuration) {
  val zkConnectionUrl = config.get[String]("app.zookeeper.url")

  val zk = ZkUtils(zkConnectionUrl, 30000, 30000, false)

  def waitUntilConnected: Boolean = {
    zk.zkClient.waitUntilConnected(30000, TimeUnit.MILLISECONDS)
  }

  def getTopics: List[Topic] = {
    val topics = zk.getAllTopics().toList

    zk.getPartitionAssignmentForTopics(topics).map { topic =>
      val partitions = topic._2.map { partition =>
        val leader = zk.getLeaderForPartition(topic._1, partition._1)
        val isr = zk.getInSyncReplicasForPartition(topic._1, partition._1)
        Partition(partition._1, leader.getOrElse(-1), partition._2.toList, isr.toList)
      }
      Topic(topic._1, partitions.toList)
    }.toList
  }

  def createTopic(topic: String, partitions: Int, replicationFactor: Int): Try[Unit] = Try({
    AdminUtils.createTopic(zk, topic, partitions, replicationFactor)
  })

  def deleteTopic(topic: String): Try[Unit] = Try({
    AdminUtils.deleteTopic(zk, topic)
  })
}
