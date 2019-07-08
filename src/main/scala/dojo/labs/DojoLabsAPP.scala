package dojo.labs

import java.time.Duration
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{KStream, ValueMapper}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig, Topology}
import play.api.libs.json.{JsObject, JsValue, Json, Writes}
import java.util.Properties

object DojoLabsAPP {
  val TOPIC_UPNP_PROTOCOL_RAW = "upnp_protocol_raw"
  val TOPIC_UPNP_PROTOCOL_TYPE = "upnp_protocol_type"
  val WORD_STRING_GIVEN = "iPhone"

  def main(args: Array[String]): Unit = {

    ///setup configurations
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass.getName)
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass.getName)
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "DojoLabsAPP")
    run(props)
  }

  /**
    * run the program given properties, creates the topology start the stream
    *
    * @param props proprties for configuration
    */
  private def run(props: java.util.Properties): Unit = {
    val topology = createTopolgy()
    val streams: KafkaStreams = new KafkaStreams(topology, props)
    streams.start()
    sys.ShutdownHookThread {
      streams.close(Duration.ofSeconds(600))
    }
  }

  /**
    * creating topology by which kafka stream will behave
    *
    * @return Topology
    */
  def createTopolgy(): Topology = {
    val builder: StreamsBuilder = new StreamsBuilder
    ////mesagge sent from kafka console will be written to the TOPIC_UPNP_PROTOCOL_RAW topic(input)
    val incomingMessageStream: KStream[String, String] = builder.stream[String, String](TOPIC_UPNP_PROTOCOL_RAW)

    //Transform String Records to JSON Objects by logic
    val newStream = incomingMessageStream.mapValues(new ValueMapper[String, String]() {
      override def apply(value: String): String = {
        var json: JsValue = Json.parse(value)
        val data = json("data")
        //case data field does not exist, a type field wont be created
        if (data != null) {
          if (data.toString().contains(WORD_STRING_GIVEN)) {
            json = withField(json.as[JsObject], "type", "Smartphone")
          }
          else {
            json = withField(json.as[JsObject], "type", "other")
          }
        }
        json.toString()
      }
    })
    //write the transformed data to the TOPIC_UPNP_PROTOCOL_TYPE topic
    newStream.mapValues(v => v).to(TOPIC_UPNP_PROTOCOL_TYPE)
    builder.build()
  }


  /**
    * updates JsObject given key and value .
    *
    * @param j     jsobject to add it key and value.
    * @param key   a key
    * @param value a value
    * @param w
    * @tparam A
    * @return
    */
  def withField[A](j: JsObject, key: String, value: A)(implicit w: Writes[A]) = {
    import play.api.libs.json.Json
    j ++ Json.obj(key -> value)
  }
}




