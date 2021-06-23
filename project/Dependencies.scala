import sbt._

object Dependencies {

  lazy val CatsEffectVersion  = "2.5.1"
  lazy val MonixVersion       = "3.0.0-M2"
  lazy val Fs2Version         = "3.0.4"
  lazy val Http4sVersion      = "0.18.0-M5"
  lazy val CirceVersion       = "0.9.0-M2"
  lazy val DoobieVersion      = "0.5.0-M9"
  lazy val SttpVersion        = "1.0.5"
  lazy val scioVersion        = "0.10.3"
  lazy val framelessVersion   = "0.9.0"
  lazy val beamVersion = "2.4.0"
  lazy val kafkaVersion = "2.6.0"
  val flinkVersion = "1.13.0"

  lazy val catsEffect     = "org.typelevel"   %% "cats-effect"          % CatsEffectVersion
  lazy val scio = "com.spotify" %% "scio-core" % scioVersion
  lazy val beamKafkaRunners = "org.apache.beam" % "beam-sdks-java-io-kafka" % beamVersion
  lazy val kafkaClient = "org.apache.kafka" % "kafka-clients" % kafkaVersion
  //lazy val beamJavaRunners = "org.apache.beam" % "beam-runners-direct-java" % beamVersion
  lazy val monixEval      = "io.monix"        %% "monix-eval"           % MonixVersion

  lazy val fs2            = "co.fs2"          %% "fs2-core"             % Fs2Version

  lazy val http4sServer   = "org.http4s"      %% "http4s-blaze-server"  % Http4sVersion
  lazy val http4sSClient  = "org.http4s"      %% "http4s-blaze-client"  % Http4sVersion
  lazy val http4sDsl      = "org.http4s"      %% "http4s-dsl"           % Http4sVersion
  lazy val http4sSCirce   = "org.http4s"      %% "http4s-circe"         % Http4sVersion

  lazy val circeCore      = "io.circe"        %% "circe-core"           % CirceVersion
  lazy val circeGeneric   = "io.circe"        %% "circe-generic"        % CirceVersion

  lazy val doobieCore     = "org.tpolecat"    %% "doobie-core"          % DoobieVersion
  lazy val doobieH2       = "org.tpolecat"    %% "doobie-h2"            % DoobieVersion

  lazy val sttp           = "com.softwaremill.sttp" %% "core"                           % SttpVersion
  lazy val sttpCats       = "com.softwaremill.sttp" %% "async-http-client-backend-cats" % SttpVersion

  lazy val logback        = "ch.qos.logback"  % "logback-classic"       % "1.2.1"
  lazy val scalaTest      = "org.scalatest"   %% "scalatest"            % "3.0.3"
  lazy val simulcrum      = "com.github.mpilquist" %% "simulacrum" % "0.13.0"

  lazy val sparkCore = "org.apache.spark" %% "spark-core" % "3.1.2"
  lazy val sparkSQL = "org.apache.spark" %% "spark-sql" % "3.1.2"
  lazy val frameless = "org.typelevel" %% "frameless-dataset" % framelessVersion
  lazy val framelessCats = "org.typelevel" %% "frameless-cats" % framelessVersion
  lazy val framelessML = "org.typelevel" %% "frameless-ml" % framelessVersion
  lazy val scalaFaker = "it.bitbl" %% "scala-faker" % "0.4"
  lazy val flink = "org.apache.flink" %% "flink-scala" % flinkVersion
  lazy val flinkStreaming = "org.apache.flink" %% "flink-streaming-scala" % flinkVersion
}
