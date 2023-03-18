name := "openai-scala"
description := "Scala client for OpenAI API"

crossScalaVersions := List(Versions.scala212, Versions.scala213, Versions.scala3)

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
