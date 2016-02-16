package org.mdoc.sbt.mdoc

import com.typesafe.sbt.packager
import com.typesafe.sbt.SbtNativePackager
import sbt.Keys

object ExtNativePackager {
  lazy val settings = Seq(
    packager.Keys.maintainer := "m-doc <info@m-doc.org>",
    packager.Keys.packageDescription := s"See <${Keys.homepage.value.getOrElse("")}> for more information.",
    packager.Keys.packageSummary := Keys.description.value,
    packager.Keys.serverLoading in SbtNativePackager.Debian :=
      com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV
  )
}
