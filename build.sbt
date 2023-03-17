ThisBuild / organization := "dev.maxmelnyk"

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

lazy val root = project.in(file(".")).aggregate(client)
lazy val client = project.in(file("client"))
