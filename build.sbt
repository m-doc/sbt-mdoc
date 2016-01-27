enablePlugins(GitVersioning)

name := "sbt-mdoc"
organization := "org.m-doc"
bintrayOrganization := Some("m-doc")

sbtPlugin := true
scalaVersion := "2.10.6"

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

git.useGitDescribe := true
