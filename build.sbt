ThisBuild / scalaVersion := "3.3.3"
ThisBuild / version      := "0.1.1"
ThisBuild / name         := "2DESF"

lazy val root = project
  .in(file("."))
  .aggregate(core, examples)

lazy val core = project
  .in(file("core"))
  .settings(
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test",
    resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"
  )

lazy val examples = project
  .in(file("examples"))
  .dependsOn(core)
  .settings(
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test",
    resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"
  )

