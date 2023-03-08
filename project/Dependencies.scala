import sbt._

object Dependencies {
  private val config = Seq("com.typesafe" % "config" % "1.4.2")

  private val logging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5")

  private val circeVersion = "0.14.5"
  private val circe = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion)

  private val cats = Seq("org.typelevel" %% "cats-effect" % "3.4.8")

  private val http4sVersion = "0.23.13"
  private val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion)

  private val sttpVersion = "3.8.12"
  private val sttp = Seq(
    "com.softwaremill.sttp.client3" %% "core" % sttpVersion,
    "com.softwaremill.sttp.client3" %% "http4s-backend" % sttpVersion)

  val allDeps: Seq[ModuleID] = config ++ logging ++ circe ++ cats ++ http4s ++ sttp
}
