package org.mdoc

import bintray.BintrayKeys
import sbt.{ AutoPlugin, Keys }

object MdocPlugin extends AutoPlugin {

  override def requires = bintray.BintrayPlugin

  override lazy val projectSettings = Seq(
    Keys.homepage := Some(sbt.url("https://github.com/m-doc/" + Keys.name.value)),
    Keys.licenses += "Apache-2.0" -> sbt.url("http://www.apache.org/licenses/LICENSE-2.0"),
    Keys.organization := "org.m-doc",
    Keys.publishMavenStyle := true,
    BintrayKeys.bintrayOrganization := Some("m-doc")
  )
}
