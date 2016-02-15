package org.mdoc.sbt.mdoc

import sbt._

trait MdocKeys {
  lazy val mdocBintrayCredentials =
    taskKey[File]("Creates ~/.bintray/.credentials if it doesn't exist.")

  lazy val mdocBintrayDeployJson =
    taskKey[File]("Creates a Bintray Deployment descriptor for Debian packages.")

  lazy val mdocRootPackage =
    settingKey[String]("Root package.")

  lazy val mdocValidateCommands =
    settingKey[Seq[String]]("Commands that are executed by 'mdocValidate'.")
}

object MdocKeys extends MdocKeys
