package org.mdoc.sbtmdoc

import bintray.{ BintrayKeys, BintrayPlugin }
import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.SbtGit.git
import com.typesafe.sbt.SbtScalariform
import sbt._
import sbtbuildinfo.{ BuildInfoKey, BuildInfoKeys, BuildInfoPlugin }
import scoverage.ScoverageSbtPlugin

object MdocPlugin extends AutoPlugin {

  override def requires = BintrayPlugin && BuildInfoPlugin && GitVersioning &&
    SbtScalariform && ScoverageSbtPlugin

  object autoImport {
    object Version {
      val logback = "1.1.3"
      val http4s = "0.12.0"
      val scalacheck = "1.12.5"
      val scalaz = "7.1.6"
      val scodecBits = "1.0.12"
    }

    val makeBintrayCredentials = taskKey[File]("creates .bintray/.credentials if it doesn't exists")
    val rootPackage = settingKey[String]("root package")
    val validateCommands = settingKey[Seq[String]]("commands that are executed by 'validate'")
  }
  import autoImport._

  lazy val validateDef = Command.command("validate") { (state: State) =>
    val extracted = Project.extract(state)
    val commands = extracted.get(validateCommands)
    state.copy(remainingCommands = commands)
  }

  override lazy val projectSettings = Seq(
    makeBintrayCredentials := {
      val credentials = file(Path.userHome.absolutePath + "/.bintray/.credentials")
      val user = Option(System.getenv("BINTRAY_USER")).getOrElse("")
      val pass = Option(System.getenv("BINTRAY_PASSWORD")).getOrElse("")
      if (!credentials.exists()) {
        val content = s"""
          |realm = Bintray API Realm
          |host = api.bintray.com
          |user = $user
          |password = $pass
        """.stripMargin.trim
        IO.write(credentials, content)
      }
      credentials
    },
    rootPackage := s"${Keys.organization.value}.${Keys.name.value}".replaceAll("-", ""),
    validateCommands := Seq(
      "clean",
      "coverage",
      "compile",
      "test",
      "coverageReport",
      "scalastyle",
      "test:scalastyle",
      "doc"
    ),
    Keys.commands += validateDef,
    Keys.initialCommands := s"import ${rootPackage.value}._",
    Keys.homepage := Some(githubUrl(Keys.name.value)),
    Keys.licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
    Keys.organization := "org.m-doc",
    Keys.publishMavenStyle := true,
    Keys.resolvers += "m-doc/maven" at "https://dl.bintray.com/m-doc/maven",
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
    Keys.scalacOptions in (Compile, Keys.doc) ++= Seq(
      "-diagrams",
      "-diagrams-debug",
      "-doc-source-url", githubUrl(Keys.name.value) + "/tree/master€{FILE_PATH}.scala",
      "-sourcepath", Keys.baseDirectory.in(LocalRootProject).value.getAbsolutePath
    ),
    Keys.scalaVersion := "2.11.7",
    Keys.scmInfo := Some(ScmInfo(
      githubUrl(Keys.name.value),
      s"scm:git:${gitUrl(Keys.name.value)}",
      Some(s"scm:git:${gitDevUrl(Keys.name.value)}")
    )),
    BintrayKeys.bintrayOrganization := Some("m-doc"),
    BuildInfoKeys.buildInfoPackage := rootPackage.value,
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
