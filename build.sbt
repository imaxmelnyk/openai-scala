name := "openai-scala"
organization := "dev.maxmelnyk"

lazy val scala212 = "2.12.17"
lazy val scala213 = "2.13.10"
lazy val scala3 = "3.2.2"
lazy val supportedScalaVersions = List(scala212, scala213, scala3)

scalaVersion := scala213
crossScalaVersions := supportedScalaVersions
scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) => List("-language:higherKinds")
    case _ => List.empty
  }
}

libraryDependencies ++= Dependencies.commonDeps
libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) => Dependencies.scala2Deps
    case _ => List.empty
  }
}

// additional info for public releases
versionScheme := Some("early-semver")
description := "Scala client for OpenAI API"
organizationHomepage := Some(url("https://maxmelnyk.dev"))
homepage := Some(url("https://github.com/imaxmelnyk/openai-scala"))
licenses := Seq("MIT" -> url("https://choosealicense.com/licenses/mit"))
developers := List(Developer("imaxmelnyk", "Max Melnyk", "max@maxmelnyk.dev", url("https://maxmelnyk.dev")))
scmInfo := Some(ScmInfo(url("https://github.com/imaxmelnyk/openai-scala"), "scm:git:https://github.com/imaxmelnyk/openai-scala.git"))

// sonatype specifics
import xerial.sbt.Sonatype._
sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
sonatypeProjectHosting := Some(GitHubHosting("imaxmelnyk", "openai-scala", "max@maxmelnyk.dev"))

// custom test report location for cross versions
Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-u", s"target/test-reports/scala-${scalaVersion.value}")
