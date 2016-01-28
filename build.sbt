enablePlugins(GitVersioning)

name := "sbt-mdoc"
organization := "org.m-doc"
bintrayOrganization := Some("m-doc")
licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")

sbtPlugin := true
scalaVersion := "2.10.6"

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

git.useGitDescribe := true

publishMavenStyle := false
bintrayRepository := "sbt-plugins"
