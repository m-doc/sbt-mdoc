package org.mdoc.sbt.mdoc

import sbt._

object Library {
  val commonModel = "org.m-doc" %% "common-model" % Version.commonModel
  val fshell = "org.m-doc" %% "fshell" % Version.fshell
  val renderingEngines = "org.m-doc" %% "rendering-engines" % Version.renderingEngines

  val circeCore = "io.circe" %% "circe-core" % Version.circe
  val circeGeneric = "io.circe" %% "circe-generic" % Version.circe
  val http4sCirce = "org.http4s" %% "http4s-circe" % Version.http4s
  val http4sCore = "org.http4s" %% "http4s-core" % Version.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Version.http4s
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Version.http4s
  val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.logback
  val properly = "eu.timepit" %% "properly" % Version.properly
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging
  val scalacheck = "org.scalacheck" %% "scalacheck" % Version.scalacheck
  val scalazCore = "org.scalaz" %% "scalaz-core" % Version.scalaz
  val scalazConcurrent = "org.scalaz" %% "scalaz-concurrent" % Version.scalaz
  val scalazEffect = "org.scalaz" %% "scalaz-effect" % Version.scalaz
  val scodecBits = "org.scodec" %% "scodec-bits" % Version.scodecBits
}
