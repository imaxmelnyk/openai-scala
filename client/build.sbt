name := "openai-scala"
description := "Scala client for OpenAI API"

lazy val scala212 = "2.12.17"
lazy val scala213 = "2.13.10"
lazy val scala3 = "3.2.2"
lazy val supportedScalaVersions = List(scala212, scala213, scala3)

scalaVersion := scala213
crossScalaVersions := supportedScalaVersions

scalacOptions ++= List("-language:implicitConversions")
scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) => List("-language:higherKinds", "-Ypartial-unification")
    case _ => List.empty
  }
}

libraryDependencies ++= Dependencies.Client.commonDeps
libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) => Dependencies.Client.scala2Deps
    case _ => List.empty
  }
}

// custom test report location for cross versions
Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-u", s"target/test-reports/scala-${scalaVersion.value}")
