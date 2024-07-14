package com.pulsar.learning

import com.pulsar.learning.domain.Issue
import com.sksamuel.pulsar4s._
import com.sksamuel.pulsar4s.circe._
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object PulsarScalaApp {

  import ExecutionContext.Implicits.global

  def createClient(pulsarUrl: String) = {
    PulsarClient(pulsarUrl)
  }

  def createClientWithConfig(pulsarUrl: String): PulsarAsyncClient = {
    val config = PulsarClientConfig(pulsarUrl)
    PulsarClient(config)
  }

  def createProducer(client: PulsarClient, topic: Topic): Producer[Issue] = {
    val producerConfig = ProducerConfig(topic)
    client.producer[Issue](producerConfig)
  }

  def sendMessage(producer: Producer[Issue], issue: Issue): Try[MessageId] = {
    producer.send(issue)
  }

  def sendMessageWithProducerMessage(producer: Producer[Issue], issue: Issue, messageKey: Option[String]): Try[MessageId] = {
    val message = DefaultProducerMessage(messageKey, issue, eventTime = Some(EventTime(System.currentTimeMillis)))
    producer.send(message)
  }

  def createConsumer(client: PulsarClient, topic: Topic): Consumer[Issue] = {
    val consumerConfig = ConsumerConfig(Subscription("mysub"), Seq(topic))
    client.consumer[Issue](consumerConfig)
  }

  def sendAndReceive(consumer: Consumer[Issue], producer: Producer[Issue], issue: Issue): Try[ConsumerMessage[Issue]] = {
    sendMessage(producer, issue)
    val message = consumer.receive
    // Acknowledge received messages
    message.foreach(consumerMessage => consumer.acknowledge(consumerMessage.messageId))
    message
  }

  def sendAndReceiveAsync(consumer: Consumer[Issue], producer: Producer[Issue], issue: Issue): Future[ConsumerMessage[Issue]] = {
    sendMessage(producer, issue)
    val futMessage = consumer.receiveAsync
    // Acknowledge received messages
    futMessage.foreach(consumerMessage => consumer.acknowledge(consumerMessage.messageId))
    futMessage
  }
}
