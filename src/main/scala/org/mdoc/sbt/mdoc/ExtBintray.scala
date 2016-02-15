package org.mdoc.sbt.mdoc

import bintray.{ BintrayKeys, BintrayPlugin }
import sbt._

object ExtBintray {
  def requires = BintrayPlugin

  lazy val settings = Seq(
    BintrayKeys.bintrayOrganization := Some("m-doc"),
    MdocKeys.mdocBintrayCredentials := {
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
    MdocKeys.mdocBintrayDeployJson := {
      val subject = BintrayKeys.bintrayOrganization.value.getOrElse("")
      val licenses = Keys.licenses.value.map { case (name, _) => s""""$name"""" }.mkString(", ")
      val content = s"""
        |{
        |  "package": {
        |    "name": "${Keys.name.value}",
        |    "repo": "debian",
        |    "subject": "$subject",
        |    "website_url": "${Keys.homepage.value.getOrElse("")}",
        |    "issue_tracker_url": "${ExtGit.gitHubUrl(Keys.name.value)}/issues",
        |    "vcs_url": "${ExtGit.gitUrl(Keys.name.value)}",
        |    "licenses": [ $licenses ]
        |  },
        |  "version": {
        |    "name": "${Keys.version.value}"
        |  },
        |  "files": [
        |    {
        |      "includePattern": "${Keys.target.value}/(.*${Keys.version.value}.*\\.deb)",
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
    }
  )
}
