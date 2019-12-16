import scala.util.Random

organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `hello-world` = (project in file("."))
  .aggregate(`hello-world-stream-api`, `hello-world-stream-impl`, `hello-world-stream-client`)

lazy val `hello-world-stream-api` = (project in file("hello-world-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `hello-world-stream-impl` = (project in file("hello-world-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`hello-world-stream-api`)

lazy val `hello-world-stream-client` = (project in file("hello-world-stream-client"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`hello-world-stream-api`)

lagomKafkaEnabled in ThisBuild := false
lagomCassandraEnabled in ThisBuild := false
lagomServiceLocatorPort in ThisBuild := Random.nextInt(10000) + 50000
lagomServiceGatewayPort in ThisBuild := Random.nextInt(10000) + 50000
