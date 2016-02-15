enablePlugins(MdocPlugin)

name := "sbt-mdoc"

sbtPlugin := true
scalaVersion := "2.10.6"

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")

publishMavenStyle := false
bintrayRepository := "sbt-plugins"

validateCommands --= Seq("coverage", "coverageReport")
