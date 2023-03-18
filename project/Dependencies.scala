import sbt._

object Dependencies {
  object Client {
    private val config = Seq("com.typesafe" % "config" % "1.4.2")

    private val logging = Seq("com.typesafe.scala-logging" %% "scala-logging" % "3.9.5")

    private val circeVersion = "0.14.5"
    private val circe = Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion)

    private val cats = Seq("org.typelevel" %% "cats-core" % "2.9.0")

    private val sttp = Seq("com.softwaremill.sttp.client3" %% "core" % "3.8.13")

    private val enumeratumVersion = "1.7.2"
    private val enumeratum = Seq(
      "com.beachape" %% "enumeratum" % enumeratumVersion,
      "com.beachape" %% "enumeratum-circe" % enumeratumVersion)

    private val test = Seq(
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
      "com.vladsch.flexmark" % "flexmark-all" % "0.64.0" % Test)

    val commonDeps: Seq[ModuleID] = config ++ logging ++ circe ++ cats ++ sttp ++ enumeratum ++ test

    // deps dedicated to scala 2.x versions
    val scala2Deps: Seq[ModuleID] = Seq(
      "io.circe" %% "circe-generic-extras" % "0.14.3")
  }

  object CatsEffectHttp4sExample {
    private val catsEffect = Seq("org.typelevel" %% "cats-effect" % "3.4.8")

    private val http4sVersion = "0.23.14"
    private val http4s = Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion)

    private val sttp = Seq("com.softwaremill.sttp.client3" %% "http4s-backend" % "3.8.13")

    val deps: Seq[ModuleID] = catsEffect ++ http4s ++ sttp
  }

  object AkkaHttpExample {
    private val akkaVersion = "2.8.0"
    private val akkaHttpVersion = "10.5.0"
    private val akka = Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion)

    private val sttp = Seq("com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.8.13")

    val deps: Seq[ModuleID] = akka ++ sttp
  }
}
