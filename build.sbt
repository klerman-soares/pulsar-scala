ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.13"

lazy val root = (project in file("."))
  .settings(
    name := "PulsarScala",
  )

val pulsar4sVersion = "2.7.3"

lazy val pulsar4s       = "com.sksamuel.pulsar4s" %% "pulsar4s-core" % pulsar4sVersion
lazy val pulsar4sCirce  = "com.sksamuel.pulsar4s" %% "pulsar4s-circe" % pulsar4sVersion
lazy val scalaTest      = "org.scalatest" %% "scalatest" % "3.2.19" % "test"

libraryDependencies ++= Seq(
  pulsar4s, pulsar4sCirce, scalaTest
)
