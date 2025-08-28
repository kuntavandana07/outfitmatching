ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.18"

lazy val root = (project in file("."))
  .settings(
    name := "outfit-matcher",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.5.1",
      "org.apache.spark" %% "spark-sql" % "3.5.1",
      "org.apache.spark" %% "spark-mllib" % "3.4.1",
      "org.postgresql" % "postgresql" % "42.7.1",
      "com.typesafe" % "config" % "1.4.2",
      "org.apache.logging.log4j" % "log4j-core" % "2.20.0",
      "org.apache.logging.log4j" % "log4j-api" % "2.20.0",
      "org.scalatest" %% "scalatest" % "3.2.17" % Test
    )
  )