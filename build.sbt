sbtPlugin := true

name := "sbt-scala-js-map"

organization := "com.thoughtworks.sbt-scala-js-map"

releasePublishArtifactsAction := PgpKeys.publishSigned.value

import ReleaseTransformations._

description := "A Sbt plugin that setup source mapping for Scala.js projects hosted on Github"

homepage := Some(url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""))

startYear := Some(2015)

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scmInfo := Some(ScmInfo(
  url(raw"""https://github.com/ThoughtWorksInc/${name.value}"""),
  raw"""scm:git:https://github.com/ThoughtWorksInc/${name.value}.git""",
  Some(raw"""scm:git:git@github.com:ThoughtWorksInc/${name.value}.git""")))

pomExtra :=
  <developers>
    <developer>
      <id>Atry</id>
      <name>杨博</name>
      <timezone>+8</timezone>
      <email>pop.atry@gmail.com</email>
    </developer>
  </developers>

scalacOptions += "-deprecation"

releaseProcess := {
  releaseProcess.value.patch(releaseProcess.value.indexOf(pushChanges), Seq[ReleaseStep](releaseStepCommand("sonatypeRelease")), 0)
}

releaseProcess -= runClean

releaseProcess -= runTest

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.8")

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.3.0.201604071810-r"
