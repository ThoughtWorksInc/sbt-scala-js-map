import scala.sys.process._

val gitDir = new java.io.File(".git")
if (!gitDir.exists()) {
  require(Seq("git", "init").! == 0)
  require(
    Seq("git", "-c", "user.name=Test", "-c", "user.email=test@example.com",
      "commit", "--allow-empty", "-m", "Initial commit").! == 0
  )
  require(Seq("git", "remote", "add", "origin", "https://github.com/example/project.git").! == 0)
}

libraryDependencies += Defaults.sbtPluginExtra(
  "com.thoughtworks.sbt-scala-js-map" % "sbt-scala-js-map" % sys.props("plugin.version"),
  (update / sbtBinaryVersion).value,
  (update / scalaBinaryVersion).value
)
