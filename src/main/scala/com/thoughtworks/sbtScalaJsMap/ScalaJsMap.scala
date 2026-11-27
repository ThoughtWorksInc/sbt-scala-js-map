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
import org.eclipse.jgit.transport.URIish

object ScalaJsMap extends AutoPlugin {

  object autoImport {

    val gitRemoteUris =
      settingKey[Seq[URIish]]("The remote git URIs to host the source code.")
    val isLatestSourcePushed =
      settingKey[Boolean](
        "Determine if the latest source files have been pushed to the remote git repository."
      )
    val scalaJsMapToGithubScalacOptions = settingKey[Seq[String]](
      "The scalac options to create Scala.js source map to the corresponding Github URI if any."
    )

  }

  import autoImport._

  override def globalSettings: Seq[Setting[_]] = Seq(
    gitRemoteUris := Seq.empty,
    scalaJsMapToGithubScalacOptions := Seq.empty
  )

  override final def projectSettings =
    Seq(
      gitRemoteUris ++= {
        val repositoryBuilder =
          new FileRepositoryBuilder().findGitDir(sourceDirectory.value)
        if (repositoryBuilder.getGitDir == null) {
          Seq.empty
        } else {
          val repository = repositoryBuilder.build()
          try {
            new RemoteConfig(
              repository.getConfig,
              DEFAULT_REMOTE_NAME
            ).getURIs.asScala
          } finally {
            repository.close()
          }
        }
      },
      scalaJsMapToGithubScalacOptions ++= {
        val repositoryBuilder =
          new FileRepositoryBuilder().findGitDir(sourceDirectory.value)
        val repository = repositoryBuilder.build()
        val head =
          try {
            repository.resolve(HEAD)
          } finally {
            repository.close()
          }
        if (repositoryBuilder.getGitDir == null) {
          None
        } else {
          gitRemoteUris.value.collectFirst {
            case url if url.getHost == "github.com" =>
              val path = url.getPath
              val slug = if (path.endsWith(DOT_GIT_EXT)) {
                path.substring(0, path.length - DOT_GIT_EXT.length)
              } else {
                path
              }
              val key = CrossVersion.partialVersion(scalaVersion.value) match {
                case Some((3, _)) =>
                  "-scalajs-mapSourceURI"
                case _ =>
                  "-P:scalajs:mapSourceURI"
              }
              raw"""$key:${repositoryBuilder.getWorkTree.toURI}->https://github.com/$slug/raw/${head.name}/"""
          }
        }
      },
      isLatestSourcePushed := {
        def isCi = sys.env.contains("CI")
        def isLatestSourcePushedFromWorkTree = {
          val repositoryBuilder =
            new FileRepositoryBuilder().findGitDir(sourceDirectory.value)
          if (repositoryBuilder.getGitDir == null) {
            false
          } else {
            val repository = repositoryBuilder.build()
            try {
              val git = new Git(repository)
              try {
                def isClean = git.status().call().isClean
                def unreachableOriginBranches = {
                  val head = repository.resolve(HEAD)
                  val revWalk = new RevWalk(repository)
                  try {
                    RevWalkUtils
                      .findBranchesReachableFrom(
                        revWalk.lookupCommit(head),
                        revWalk,
                        repository.getRefDatabase
                          .getRefsByPrefix(s"$R_REMOTES$DEFAULT_REMOTE_NAME/")
                      )
                      .isEmpty
                  } finally {
                    revWalk.close()
                  }
                }
                isClean && !unreachableOriginBranches
              } finally {
                git.close()
              }
            } finally {
              repository.close()
            }
          }
        }
        isCi || isLatestSourcePushedFromWorkTree
      },
      scalacOptions ++= {
        if (isLatestSourcePushed.value) {
          scalaJsMapToGithubScalacOptions.value
        } else {
          Seq.empty
        }
      }
    )

  override final def requires = ScalaJSPlugin

  override def trigger = allRequirements

}

// vim: set ts=2 sw=2 et:
