package org.mdoc

import bintray.BintrayKeys
import sbt.{AutoPlugin, Keys}

object MdocPlugin extends AutoPlugin {

  override def requires = bintray.BintrayPlugin

  override lazy val projectSettings = Seq(
    Keys.organization := "org.m-doc",
    BintrayKeys.bintrayOrganization := Some("m-doc")
  )
}
