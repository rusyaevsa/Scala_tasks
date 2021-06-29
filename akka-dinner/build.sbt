name := "akka-dinner"

version := "0.1"

scalaVersion := "2.13.6"


val AkkaVersion = "2.6.9"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"