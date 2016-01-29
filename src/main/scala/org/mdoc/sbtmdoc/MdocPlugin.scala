package org.mdoc.sbtmdoc

import bintray.{ BintrayKeys, BintrayPlugin }
import com.typesafe.sbt.SbtGit.git
import com.typesafe.sbt.{ GitVersioning, SbtScalariform }
import sbt._
import sbtbuildinfo.{ BuildInfoKey, BuildInfoKeys, BuildInfoPlugin }

object MdocPlugin extends AutoPlugin {

  override def requires = BintrayPlugin && BuildInfoPlugin && GitVersioning && SbtScalariform

  object autoImport {
    object Version {
      val scalacheck = "1.12.5"
      val scalaz = "7.1.6"
      val scodecBits = "1.0.12"
    }

    val rootPackage = settingKey[String]("root package")
  }
  import autoImport._

  override lazy val projectSettings = Seq(
    rootPackage := s"${Keys.organization.value}.${Keys.name.value}".replaceAll("-", ""),
    Keys.initialCommands := s"import ${rootPackage.value}._",
    Keys.homepage := Some(githubUrl(Keys.name.value)),
    Keys.licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
    Keys.organization := "org.m-doc",
    Keys.publishMavenStyle := true,
    Keys.scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xfuture",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard"
    ),
    Keys.scalaVersion := "2.11.7",
    Keys.scmInfo := Some(ScmInfo(
      githubUrl(Keys.name.value),
      s"scm:git:${gitUrl(Keys.name.value)}",
      Some(s"scm:git:${gitDevUrl(Keys.name.value)}")
    )),
    BintrayKeys.bintrayOrganization := Some("m-doc"),
    BuildInfoKeys.buildInfoKeys := Seq[BuildInfoKey](
      Keys.name,
      Keys.version,
      Keys.scalaVersion,
      BuildInfoKey.map(Keys.homepage) { case (k, v) => k -> v.fold("")(_.toString) },
      BuildInfoKey.map(git.gitHeadCommit) { case (k, v) => k -> v.getOrElse("") }
    ),
    git.useGitDescribe := true
  )

  def githubUrl(projectName: String): URL =
    url(s"https://github.com/m-doc/$projectName")

  def gitUrl(projectName: String): String =
    s"https://github.com/m-doc/$projectName.git"

  def gitDevUrl(projectName: String): String =
    s"git@github.com:m-doc/$projectName.git"
}
