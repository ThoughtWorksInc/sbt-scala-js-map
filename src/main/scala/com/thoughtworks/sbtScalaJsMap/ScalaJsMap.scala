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

import java.net._

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ConfigConstants._
import org.eclipse.jgit.lib.Constants._
import org.eclipse.jgit.revwalk.{RevWalk, RevWalkUtils}
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.transport.RemoteConfig
import org.scalajs.sbtplugin.ScalaJSPlugin
import sbt.Keys._
import sbt._

import scala.collection.JavaConverters._

object ScalaJsMap extends AutoPlugin {

  override final def projectSettings =
    Seq(scalacOptions ++= {
      val repositoryBuilder = new FileRepositoryBuilder().findGitDir(sourceDirectory.value)
      if (repositoryBuilder.getGitDir == null) {
        None
      } else {
        val repository = repositoryBuilder.build()
        try {
          val git = new Git(repository)
          try {
            if (git.status().call().isClean) {
              val head = repository.resolve(HEAD)
              val unreachableOriginBranches = {
                val revWalk = new RevWalk(repository)
                try {
                  RevWalkUtils
                    .findBranchesReachableFrom(
                      revWalk.lookupCommit(head),
                      revWalk,
                      repository.getRefDatabase.getRefsByPrefix(s"$R_REMOTES$DEFAULT_REMOTE_NAME/")
                    )
                    .isEmpty

                } finally {
                  revWalk.close()
                }
              }

              if (unreachableOriginBranches) {
                None
              } else {
                new RemoteConfig(repository.getConfig, DEFAULT_REMOTE_NAME).getURIs.asScala.collectFirst {
                  case url if url.getHost == "github.com" =>
                    val path = url.getPath
                    val slug = if (path.endsWith(DOT_GIT_EXT)) {
                      path.substring(0, path.length - DOT_GIT_EXT.length)
                    } else {
                      path
                    }
                    raw"""-P:scalajs:mapSourceURI:${repository.getWorkTree.toURI}->https://github.com/$slug/raw/${head.name}/"""
                }

              }
            } else {
              None
            }
          } finally {
            git.close()
          }
        } finally {
          repository.close()
        }
      }
    })

  private val OptionalGitSuffixRegex = """(.*?)(?:\.git)?+""".r
  private val SshUrlRegex = """git@github.com:(.*?)(?:\.git)?+""".r

  override final def requires = ScalaJSPlugin

  override def trigger = allRequirements

}

// vim: set ts=2 sw=2 et:
