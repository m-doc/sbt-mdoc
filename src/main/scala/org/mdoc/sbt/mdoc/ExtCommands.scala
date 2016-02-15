package org.mdoc.sbt.mdoc

import sbt._

object ExtCommands {
  lazy val settings = Seq(
    Keys.commands += mkCommandAlias("mdocValidate", MdocKeys.mdocValidateCommands),
    MdocKeys.mdocValidateCommands := Seq(
      "clean",
      "coverage",
      "compile",
      "test",
      "coverageReport",
      "scalastyle",
      "test:scalastyle",
      "doc"
    )
  ) ++
    addCommandAlias("mdocPublishJar", s";clean ;${MdocKeys.mdocBintrayCredentials.key.label} ;reload ;publish") ++
    addCommandAlias("mdocPublishDeb", s";clean ;${MdocKeys.mdocBintrayDeployJson.key.label} ;debian:packageBin")

  def mkCommandAlias(name: String, commandsKey: SettingKey[Seq[String]]): Command =
    Command.command(name) { (state: State) =>
      val extracted = Project.extract(state)
      val commands = extracted.get(commandsKey)
      state.copy(remainingCommands = commands ++ state.remainingCommands)
    }
}
