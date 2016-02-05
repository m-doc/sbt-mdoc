package org.mdoc.sbt.mdoc

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
      val commonModel = "0.0.0"
      val fshell = "0.0.0-29-g4b0878c"
      val renderingEngines = "0.0.0-8-g867c9f3"

      val logback = "1.1.3"
      val http4s = "0.12.1"
      val properly = "0.0.0-18-g5fef5a3"
      val scalaLogging = "3.1.0"
      val scalacheck = "1.13.0"
      val scalaz = "7.1.6"
      val scodecBits = "1.0.12"
      val slf4j = "1.7.14"
    }

    val makeBintrayCredentials =
      taskKey[File]("Creates ~/.bintray/.credentials if it doesn't exist.")

    val makeBintrayDeploymentDescriptorDeb =
      taskKey[File]("Creates Bintray Deployment descriptor for Debian packages.")

    val rootPackage =
      settingKey[String]("Root package.")

    val validateCommands =
      settingKey[Seq[String]]("Commands that are executed by 'validate'.")
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
      if (!credentials.exists()) {
        val user = Option(System.getenv("BINTRAY_USER")).getOrElse("")
        val pass = Option(System.getenv("BINTRAY_PASSWORD")).getOrElse("")
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
    makeBintrayDeploymentDescriptorDeb := {
      val subject = BintrayKeys.bintrayOrganization.value.getOrElse("")
      val licenses = Keys.licenses.value.map { case (name, _) => s""""$name"""" }.mkString(", ")
      val content = s"""
        |{
        |  "package": {
        |    "name": "${Keys.name.value}",
        |    "repo": "debian",
        |    "subject": "$subject",
        |    "website_url": "${Keys.homepage.value.getOrElse("")}",
        |    "issue_tracker_url": "${gitHubUrl(Keys.name.value)}/issues",
        |    "vcs_url": "${gitUrl(Keys.name.value)}",
        |    "licenses": [ $licenses ]
        |  },
        |  "version": {
        |    "name": "${Keys.version.value}"
        |  },
        |  "files": [
        |    {
        |      "includePattern": "target/(.*${Keys.version.value}.*\\.deb)",
        |      "uploadPattern": "$$1",
        |      "matrixParams": {
        |        "deb_distribution": "stable",
        |        "deb_component": "main",
        |        "deb_architecture": "all"
        |      }
        |    }
        |  ],
        |  "publish": true
        |}
      """.stripMargin.trim
      val descriptor = file("deploy.json")
      IO.write(descriptor, content)
      descriptor
    },
    rootPackage := s"${Keys.organization.value.replaceAll("-", "")}.${Keys.name.value.replaceAll("-", ".")}",
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
    Keys.homepage := Some(gitHubUrl(Keys.name.value)),
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
      "-doc-source-url", gitHubUrl(Keys.name.value) + "/tree/master€{FILE_PATH}.scala",
      "-sourcepath", Keys.baseDirectory.in(LocalRootProject).value.getAbsolutePath
    ),
    Keys.scalaVersion := "2.11.7",
    Keys.scmInfo := Some(ScmInfo(
      gitHubUrl(Keys.name.value),
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

  def gitHubUrl(projectName: String): URL =
    url(s"https://github.com/m-doc/$projectName")

  def gitUrl(projectName: String): String =
    s"https://github.com/m-doc/$projectName.git"

  def gitDevUrl(projectName: String): String =
    s"git@github.com:m-doc/$projectName.git"
}