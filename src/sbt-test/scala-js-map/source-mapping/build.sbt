enablePlugins(ScalaJSPlugin)

@transient val checkMapping = taskKey[Unit]("Check the GitHub source mapping")
@transient val checkDisabled =
  taskKey[Unit]("Check that an unpublished source is not mapped")

crossScalaVersions := Seq("2.12.21", "3.9.0")

isLatestSourcePushed := true

checkMapping := {
  val options = (Compile / scalacOptions).value
  val key =
    if (scalaVersion.value.startsWith("3.")) "-scalajs-mapSourceURI"
    else "-P:scalajs:mapSourceURI"
  val mappings = scalaJsMapToGithubScalacOptions.value
  assert(
    gitRemoteUris.value.exists(
      _.toString == "https://github.com/example/project.git"
    )
  )
  assert(mappings.size == 1 && mappings.head.startsWith(s"$key:"))
  assert(mappings.head.contains("->https://github.com/"))
  assert(mappings.head.contains("example/project/raw/"))
  assert(options.contains(mappings.head))
}

checkDisabled := {
  assert(!isLatestSourcePushed.value)
  assert(
    !scalaJsMapToGithubScalacOptions.value.exists(
      (Compile / scalacOptions).value.contains
    )
  )
}
