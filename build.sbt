enablePlugins(MdocPlugin)

name := "sbt-mdoc"

sbtPlugin := true
scalaVersion := "2.10.6"

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.5.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

publishMavenStyle := false
bintrayRepository := "sbt-plugins"
