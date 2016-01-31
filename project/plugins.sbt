logLevel := Level.Warn

addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.7.9")

resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.3")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.3")