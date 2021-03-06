package org.mdoc.sbt.mdoc

import com.typesafe.sbt.SbtScalariform
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._
import scoverage.ScoverageSbtPlugin

object MdocPlugin extends AutoPlugin {
  override def requires =
    ExtBintray.requires &&
      ExtBuildInfo.requires &&
      ExtGit.requires &&
      SbtScalariform &&
      ScoverageSbtPlugin

  object autoImport extends MdocKeys {
    val Library = org.mdoc.sbt.mdoc.Library
    val Version = org.mdoc.sbt.mdoc.Version
    val noPublishSettings = Seq(
      Keys.publish := {},
      Keys.publishLocal := {},
      Keys.publishArtifact := false
    )

    implicit final class MdocModuleIdOps(val self: ModuleID) extends AnyVal {
      def js: Def.Initialize[ModuleID] =
        Def.setting(self.organization %%% self.name % self.revision)
    }

    def addCommandsAlias(name: String, cmds: Seq[String]): Seq[Setting[State => State]] =
      addCommandAlias(name, cmds.mkString(";", ";", ""))
  }

  override lazy val projectSettings = Seq(
    Keys.initialCommands := s"import ${MdocKeys.mdocRootPackage.value}._",
    Keys.homepage := Some(ExtGit.gitHubUrl(Keys.name.value)),
    Keys.licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
    Keys.organization := "org.m-doc",
    Keys.publishMavenStyle := true,
    Keys.resolvers ++= Seq(
      "fthomas/maven" at "https://dl.bintray.com/fthomas/maven",
      "m-doc/maven" at "https://dl.bintray.com/m-doc/maven",
      "pellucid/maven" at "https://dl.bintray.com/pellucid/maven"
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
      "-doc-source-url", ExtGit.gitHubUrl(Keys.name.value) + "/tree/master€{FILE_PATH}.scala",
      "-sourcepath", Keys.baseDirectory.in(LocalRootProject).value.getAbsolutePath
    ),
    Keys.scmInfo := Some(ScmInfo(
      ExtGit.gitHubUrl(Keys.name.value),
      s"scm:git:${ExtGit.gitUrl(Keys.name.value)}",
      Some(s"scm:git:${ExtGit.gitDevUrl(Keys.name.value)}")
    )),
    MdocKeys.mdocRootPackage :=
      Keys.organization.value.replaceAll("-", "") + "." + Keys.name.value.replaceAll("-", ".")
  ) ++
    ExtBintray.settings ++
    ExtBuildInfo.settings ++
    ExtCommands.settings ++
    ExtGit.settings ++
    ExtNativePackager.settings
}
