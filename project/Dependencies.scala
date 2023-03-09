import sbt._

object Dependencies {
  private val config = Seq("com.typesafe" % "config" % "1.4.2")

  private val logging = Seq("com.typesafe.scala-logging" %% "scala-logging" % "3.9.5")

  private val circeVersion = "0.14.3"
  private val circe = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion)

  private val cats = Seq("org.typelevel" %% "cats-effect" % "3.4.8")

  private val sttp = Seq("com.softwaremill.sttp.client3" %% "core" % "3.8.12")

  private val test = Seq("org.scalatest" %% "scalatest" % "3.2.15" % Test)

  val allDeps: Seq[ModuleID] = config ++ logging ++ circe ++ cats ++ sttp ++ test
}
