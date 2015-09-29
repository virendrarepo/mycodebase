import AssemblyKeys._

name := "recs-api"

version := "1.0"

libraryDependencies ++= Seq(
	"org.slf4j" % "slf4j-api" % "1.7.5",
	"ch.qos.logback" % "logback-classic" % "1.0.13",
	"com.typesafe.akka" %% "akka-actor" % "2.3.4",
	"com.typesafe.akka" %% "akka-slf4j" % "2.3.4",
	"io.spray" %% "spray-can" % "1.3.1",
	"io.spray" %% "spray-routing" % "1.3.1",
	"io.spray" %%  "spray-json" % "1.2.5",
	"io.spray" %%  "spray-client" % "1.3.1",
	"io.spray" %%  "spray-httpx" % "1.3.1",
	"org.scalatest" %% "scalatest" % "2.2.0" % "test"
)

assemblySettings

mainClass in assembly := Some("com.sky.assignment.Application")
    