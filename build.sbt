ThisBuild / organization := "dev.maxmelnyk"

ThisBuild / scalaVersion := Versions.scala213

// info for public releases
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / organizationHomepage := Some(url("https://maxmelnyk.dev"))
ThisBuild / homepage := Some(url("https://github.com/imaxmelnyk/openai-scala"))
ThisBuild / licenses := Seq("MIT" -> url("https://choosealicense.com/licenses/mit"))
ThisBuild / developers := List(Developer("imaxmelnyk", "Max Melnyk", "max@maxmelnyk.dev", url("https://maxmelnyk.dev")))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/imaxmelnyk/openai-scala"), "scm:git:https://github.com/imaxmelnyk/openai-scala.git"))

// sonatype specifics
import xerial.sbt.Sonatype._
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("imaxmelnyk", "openai-scala", "max@maxmelnyk.dev"))

// don't publish this project
publish / skip := true

lazy val root = project.in(file(".")).aggregate(client, catsEffectHttp4sExample)
lazy val client = project.in(file("client"))
lazy val catsEffectHttp4sExample = project.in(file("examples/cats-effect-http4s")).dependsOn(client)
