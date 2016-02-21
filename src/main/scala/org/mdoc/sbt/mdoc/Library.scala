package org.mdoc.sbt.mdoc

import sbt._

object Library {
  val bootstrap = "org.webjars" % "bootstrap" % Version.bootstrap
  val circeCore = "io.circe" %% "circe-core" % Version.circe
  val circeGeneric = "io.circe" %% "circe-generic" % Version.circe
  val circeParse = "io.circe" %% "circe-parse" % Version.circe
  val http4sCirce = "org.http4s" %% "http4s-circe" % Version.http4s
  val http4sCore = "org.http4s" %% "http4s-core" % Version.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Version.http4s
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Version.http4s
  val jquery = "org.webjars.npm" % "jquery" % Version.jquery
  val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.logback
  val properly = "eu.timepit" %% "properly" % Version.properly
  val react = "org.webjars.npm" % "react" % Version.react
  val reactDom = "org.webjars.npm" % "react-dom" % Version.react
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging
  val scalacheck = "org.scalacheck" %% "scalacheck" % Version.scalacheck
  val scalazCore = "org.scalaz" %% "scalaz-core" % Version.scalaz
  val scalazConcurrent = "org.scalaz" %% "scalaz-concurrent" % Version.scalaz
  val scalazEffect = "org.scalaz" %% "scalaz-effect" % Version.scalaz
  val scodecBits = "org.scodec" %% "scodec-bits" % Version.scodecBits
  val webjarsPlay = "org.webjars" %% "webjars-play" % Version.webjarsPlay
}
