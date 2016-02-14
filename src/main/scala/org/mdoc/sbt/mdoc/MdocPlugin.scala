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
    val Library = org.mdoc.sbt.mdoc.Library
    val Version = org.mdoc.sbt.mdoc.Version

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

  val commandAliases = Seq(
    addCommandAlias("mdocPublishJar", ";clean ;makeBintrayCredentials ;reload ;publish"),
    addCommandAlias("mdocPublishDeb", ";clean ;makeBintrayDeploymentDescriptorDeb ;debian:packageBin")
  ).flatten

  lazy val validateDef = Command.command("validate") { (state: State) =>
    val extracted = Project.extract(state)
    val commands = extracted.get(validateCommands)
    state.copy(remainingCommands = commands ++ state.remainingCommands)
  }

  override lazy val projectSettings = Seq(
    makeBintrayCredentials := {
      val credentialsFile = file(Path.userHome.absolutePath + "/.bintray/.credentials")
      if (!credentialsFile.exists()) {
        val user = Option(System.getenv("BINTRAY_USER")).getOrElse("")
        val pass = Option(System.getenv("BINTRAY_PASSWORD")).getOrElse("")
        val content = s"""
          |realm = Bintray API Realm
          |host = api.bintray.com
          |user = $user
          |password = $pass
        """.stripMargin.trim
        IO.write(credentialsFile, content)
      }
      credentialsFile
    },
    makeBintrayDeploymentDescriptorDeb := {
      val subject = BintrayKeys.bintrayOrganization.value.getOrElse("")
      val licenses = Keys.licenses.value.map { case (name, _) => s""""$name"""" }.mkString(", ")
      val target = Keys.target.in(ThisProject).value

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
        |      "includePattern": "$target/(.*${Keys.version.value}.*\\.deb)",
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
      descriptor.delete()
      IO.write(descriptor, content)
      descriptor
    },
    rootPackage := Keys.organization.value.replaceAll("-", "") + "." + Keys.name.value.replaceAll("-", "."),
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
    Keys.resolvers ++= Seq(
      "m-doc/maven" at "https://dl.bintray.com/m-doc/maven",
      "fthomas/maven" at "https://dl.bintray.com/fthomas/maven"
    ),
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
  ) ++ commandAliases

  def gitHubUrl(projectName: String): URL =
    url(s"https://github.com/m-doc/$projectName")

  def gitUrl(projectName: String): String =
    s"https://github.com/m-doc/$projectName.git"

  def gitDevUrl(projectName: String): String =
    s"git@github.com:m-doc/$projectName.git"
}
