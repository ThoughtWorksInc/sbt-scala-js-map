enablePlugins(ScalaJSPlugin)

val checkMapping = taskKey[Unit]("Check the GitHub source mapping")
val checkDisabled = taskKey[Unit]("Check that an unpublished source is not mapped")

isLatestSourcePushed := true

checkMapping := {
  val options = (Compile / scalacOptions).value
  val key = if (scalaVersion.value.startsWith("3.")) "-scalajs-mapSourceURI" else "-P:scalajs:mapSourceURI"
  val mappings = scalaJsMapToGithubScalacOptions.value
  assert(gitRemoteUris.value.exists(_.toString == "https://github.com/example/project.git"))
  assert(mappings.size == 1 && mappings.head.startsWith(s"$key:"))
  assert(mappings.head.contains("->https://github.com/example/project/raw/"))
  assert(options.contains(mappings.head))
}

checkDisabled := {
  assert(!isLatestSourcePushed.value)
  assert(!scalaJsMapToGithubScalacOptions.value.exists((Compile / scalacOptions).value.contains))
}
