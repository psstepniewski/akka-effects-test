
name := "akka-effects-test"

version := "0.1"

scalaVersion := "2.13.6"

val AkkaVersion = "2.6.16"
val SlickVersion = "3.3.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.5",
  "com.typesafe.akka" %% "akka-persistence-testkit" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)
