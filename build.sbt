ThisBuild / scalaVersion := "3.3.3"
ThisBuild / version      := "0.1.1"
ThisBuild / name         := "2DESF"

lazy val root = project
  .in(file("."))
  .aggregate(core, examples, view)

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

lazy val view = project
  .in(file("view"))
  .dependsOn(core, examples)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "17.0.0",
      "com.raquo" %%% "airstream" % "17.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.8.0"
    ),
    scalaJSUseMainModuleInitializer := true,
    Compile / fastOptJS / artifactPath := baseDirectory.value / "target/scala-3.3.3/main.js" // Corretto il percorso del file
  )

lazy val benchmarks = project
  .in(file("benchmarks"))
  .dependsOn(core)
  .enablePlugins(JmhPlugin)
