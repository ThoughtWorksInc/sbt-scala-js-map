/*
Copyright 2016 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.thoughtworks.sbtScalaJsMap

import sbt._
import Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.eclipse.jgit.lib.ConfigConstants._
import org.eclipse.jgit.lib.Constants._
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.net._

object ScalaJsMap extends AutoPlugin {

  private val OptionalGitSuffixRegex = """(.*?)(?:\.git)?+""".r

  private val SshUrlRegex = """git@github.com:(.*?)(?:\.git)?+""".r

  override final def requires = ScalaJSPlugin

  override final lazy val projectSettings = Seq(
    scalacOptions += {
      val repository = new FileRepositoryBuilder().findGitDir(sourceDirectory.value).build()
      raw"""-P:scalajs:mapSourceURI:${
        repository.getWorkTree.toURI
      }->https://github.com/${
        val remoteOriginUrl = repository.getConfig.getString(CONFIG_KEY_REMOTE, "origin", CONFIG_KEY_URL)
        remoteOriginUrl match {
          case SshUrlRegex(slug) =>
            slug
          case _ =>
            try {
              val url = new URL(remoteOriginUrl)
              if (url.getHost == "github.com") {
                val OptionalGitSuffixRegex(slug) = url.getPath
                slug
              } else {
                throw new MessageOnlyException(s"The code base should be cloned from Github, not $remoteOriginUrl")
              }
            } catch {
              case _: MalformedURLException =>
                throw new MessageOnlyException(s"The code base should be cloned from Github, not $remoteOriginUrl")
            }
        }
      }/raw/${
        repository.resolve(HEAD).name
      }/"""
    })

}

// vim: set ts=2 sw=2 et:
