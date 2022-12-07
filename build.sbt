sbtPlugin := true

name := "sbt-scala-js-map"

organization := "com.thoughtworks.sbt-scala-js-map"

description := "A Sbt plugin that setup source mapping for Scala.js projects hosted on Github"

startYear := Some(2016)

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.10.1")

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "6.4.0.202211300538-r"
