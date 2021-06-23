import faker.Lorem.paragraph
import org.apache.kafka.clients.consumer.{KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

import java.util.Properties
import scala.collection.JavaConverters._

object KafkaProperties {

  val properties: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", classOf[StringSerializer])
    props.put("value.serializer", classOf[StringSerializer])
    props.put("key.deserializer", classOf[StringDeserializer])
    props.put("value.deserializer", classOf[StringDeserializer])
    props.put("group.id", "TestGroup")
    props.put("transactional.id", "trans-id")

    props
  }

  val wordTopic = "test"

}

object Producer {

  def runNow(n: Int) = {
    val producer = new KafkaProducer[String, String](KafkaProperties.properties)
    producer.initTransactions()
    producer.beginTransaction()
    for (i <- (0 to n).toList) {
      val record = new ProducerRecord[String, String](KafkaProperties.wordTopic,  paragraph())
      producer send record
    }
    producer.commitTransaction()
    producer.close()
  }

}

object Consumer {
  def runNow(history: Option[List[(String, Int)]] = None): List[(String, Int)] = {
    val consumer = new KafkaConsumer[String, String](KafkaProperties.properties)
    consumer.subscribe(java.util.Arrays.asList(KafkaProperties.wordTopic))
    var newRecords = consumer.poll(java.time.Duration.ofMillis(1000)).asScala.toList
    var allRecords = newRecords
    while(newRecords.nonEmpty) {
      newRecords = consumer.poll(java.time.Duration.ofMillis(1000)).asScala.toList
      allRecords ++= newRecords
      newRecords
    }

    val hist = history.getOrElse(List[(String, Int)]())
    println(s"RECORDS:${allRecords.length}")
    val newHist = allRecords.map(_.value())
      .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty).map(_.toLowerCase))
      .map((_,1))
      .union(hist)
      .groupBy(_._1)
      .mapValues{r => r.map(_._2).sum}
      .toList.sortBy(-_._2)
    consumer.commitSync()
    consumer.close()
    newHist
  }

}


Producer.runNow(10000)
val history0 = Consumer.runNow()
history0.take(5).foreach{case(s,i) => println(s"${s}:${i}")}

Producer.runNow(10000)
val history1 = Consumer.runNow(Some(history0))
history1.take(5).foreach{case(s,i) => println(s"${s}:${i}")}

Producer.runNow(10000)
val history2 = Consumer.runNow(Some(history1))
history2.take(5).foreach{case(s,i) => println(s"${s}:${i}")}

println("Done")



