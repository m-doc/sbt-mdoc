resolvers += Resolver.url("m-doc/sbt-plugins",
  url("http://dl.bintray.com/content/m-doc/sbt-plugins"))(Resolver.ivyStylePatterns)

addSbtPlugin("org.m-doc" % "sbt-mdoc" % "0.0.0-61-g428b205")
