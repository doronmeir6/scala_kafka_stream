name := "KafkaStreaming"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq("org.apache.kafka" % "kafka-streams" % "2.2.1")

libraryDependencies += "javax.ws.rs" % "javax.ws.rs-api" % "2.1.1" artifacts Artifact("javax.ws.rs-api", "jar", "jar")
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.2.1"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.1.0"

//TEST
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.apache.kafka" % "kafka-streams-test-utils" % "2.1.0" % Test

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.2"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.2"
