package com.pulsar.learning

import com.pulsar.learning.domain.Issue
import com.sksamuel.pulsar4s._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class PulsarScalaAppTest extends AnyWordSpec with BeforeAndAfterEach with Matchers {

  var client: PulsarClient = _
  var producer: Producer[Issue] = _
  var consumer: Consumer[Issue] = _
  val pulsarCLusterUrl = "pulsar://localhost:6650"
  val topic: Topic = Topic("persistent://sample/ns1/topic-name")
  val issue: Issue = Issue(1, "First issue", "Some description")
  val messageKey: Option[String] = Some("myKey")

  override def afterEach(): Unit = {
    if (Option(client).isDefined) client.close() // PulsarClient doesn't implement AutoCloseable
    closeIfDefined(Option(producer))
    closeIfDefined(Option(consumer))
  }

  "ProducerConsumer" should {
    "create a PulsarClient" in {
      client = PulsarScalaApp.createClient(pulsarCLusterUrl)
      client mustBe a[PulsarClient]
    }
  }

  "ProducerConsumer" should {
    "create a PulsarClient using PulsarClientConfig" in {
      client = PulsarScalaApp.createClientWithConfig(pulsarCLusterUrl)
      client mustBe a[PulsarClient]
    }
  }

  it should {
    "create a producer for a given topic" in {
      client = PulsarScalaApp.createClient(pulsarCLusterUrl)
      producer = PulsarScalaApp.createProducer(client, topic)
      producer mustBe a[Producer[_]]
      producer.topic mustBe topic
    }
  }

  it should {
    "send an Issue message" in {
      client = PulsarScalaApp.createClient(pulsarCLusterUrl)
      producer = PulsarScalaApp.createProducer(client, topic)
      val messageId = PulsarScalaApp.sendMessage(producer, issue).get
      messageId mustBe a[MessageId]
    }
  }

  it should {
    "send an Issue message using a ProducerMessage" in {
      client = PulsarScalaApp.createClient(pulsarCLusterUrl)
      producer = PulsarScalaApp.createProducer(client, topic)
      val messageId = PulsarScalaApp.sendMessageWithProducerMessage(producer, issue, messageKey).get
      messageId mustBe a[MessageId]
    }
  }

  it should {
    "create a consumer" in {
      client = PulsarScalaApp.createClient(pulsarCLusterUrl)
      consumer = PulsarScalaApp.createConsumer(client, topic)
      consumer mustBe a[Consumer[_]]
      consumer.topic mustBe topic
    }
  }

  it should {
    "send and receive (blocking) a message" in {
      client = PulsarScalaApp.createClient(pulsarCLusterUrl)
      consumer = PulsarScalaApp.createConsumer(client, topic)
      producer = PulsarScalaApp.createProducer(client, topic)
      val messageTry = PulsarScalaApp.sendAndReceive(consumer, producer, issue)
      messageTry.get.value mustBe issue
    }
  }

  it should {
    "send and receive (async) a message" in {
      client = PulsarScalaApp.createClient(pulsarCLusterUrl)
      consumer = PulsarScalaApp.createConsumer(client, topic)
      producer = PulsarScalaApp.createProducer(client, topic)
      val messageFuture = Await.ready(PulsarScalaApp.sendAndReceiveAsync(consumer, producer, issue), Duration.Inf)
      messageFuture.foreach(message => message.value mustBe issue)
    }
  }

  def closeIfDefined[T <: AutoCloseable](resource: Option[T]): Unit = resource.foreach(_.close())
}
