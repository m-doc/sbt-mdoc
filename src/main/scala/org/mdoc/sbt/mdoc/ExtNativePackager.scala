package org.mdoc.sbt.mdoc

import com.typesafe.sbt.packager
import com.typesafe.sbt.packager.debian.DebianPlugin.autoImport.Debian
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport.Linux
import sbt.Keys

object ExtNativePackager {
  lazy val settings = Seq(
    Keys.name in Debian := s"mdoc-${Keys.name.value}",
    Keys.name in Linux := Keys.name.in(Debian).value,
    packager.Keys.executableScriptName := Keys.name.in(Debian).value,
    packager.Keys.maintainer := "m-doc <info@m-doc.org>",
    packager.Keys.packageName in Debian := Keys.name.in(Debian).value,
    packager.Keys.packageName in Linux := Keys.name.in(Debian).value,
    packager.Keys.packageDescription := s"See <${Keys.homepage.value.getOrElse("")}> for more information.",
    packager.Keys.packageSummary := Keys.description.value
  )
}
