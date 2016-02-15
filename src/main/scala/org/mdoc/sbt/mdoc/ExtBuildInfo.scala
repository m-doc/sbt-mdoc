package org.mdoc.sbt.mdoc

import com.typesafe.sbt.SbtGit.git
import sbt.Keys
import sbtbuildinfo._

object ExtBuildInfo {
  def requires = BuildInfoPlugin

  lazy val settings = Seq(
    BuildInfoKeys.buildInfoPackage := MdocKeys.mdocRootPackage.value,
    BuildInfoKeys.buildInfoUsePackageAsPath := true,
    BuildInfoKeys.buildInfoKeys := Seq[BuildInfoKey](
      Keys.name,
      Keys.version,
      Keys.scalaVersion,
      BuildInfoKey.map(Keys.homepage) { case (k, v) => k -> v.fold("")(_.toString) },
      BuildInfoKey.map(git.gitHeadCommit) { case (k, v) => k -> v.getOrElse("") }
    )
  )
}
