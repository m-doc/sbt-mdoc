package org.mdoc.sbt.mdoc

import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.SbtGit.git
import sbt._

object ExtGit {
  def requires = GitVersioning

  lazy val settings = Seq(
    git.useGitDescribe := true
  )

  def gitHubUrl(projectName: String): URL =
    url(s"https://github.com/m-doc/$projectName")

  def gitUrl(projectName: String): String =
    s"https://github.com/m-doc/$projectName.git"

  def gitDevUrl(projectName: String): String =
    s"git@github.com:m-doc/$projectName.git"
}
