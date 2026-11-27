sbtPlugin := true

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => sbtVersion.value
    case _      => "2.1.1"
  }
}

name := "sbt-scala-js-map"

organization := "com.thoughtworks.sbt-scala-js-map"

description := "A Sbt plugin that setup source mapping for Scala.js projects hosted on Github"

startYear := Some(2016)

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.23.0")

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "6.6.1.202309021850-r"

scriptedBufferLog := false

scriptedLaunchOpts += s"-Dplugin.version=${version.value}"
