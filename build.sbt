import sbt.Keys.libraryDependencies
import scala.collection.Seq

ThisBuild / scalaVersion := "3.3.3"
ThisBuild / version      := "0.1.1"
ThisBuild / name         := "2DESF"

// Progetto principale che aggrega i moduli core, examples, e view
lazy val root = project
  .in(file("."))
  .aggregate(coreJVM, coreJS, examples, view)

// Modulo core con sottoprogetti specifici per JVM e JS
lazy val core = project
  .in(file("core/shared")) // Codice condiviso tra JVM e JS
  .enablePlugins(ScalaJSPlugin)
  .settings(
  )

lazy val coreJVM = project
  .in(file("core/jvm"))
  .dependsOn(core) // Il modulo JVM dipende dal codice condiviso (shared)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.19",
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
    )
  )

lazy val coreJS = project
  .in(file("core/js"))
  .dependsOn(core) // Il modulo JS dipende dal codice condiviso (shared)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "17.0.0",
      "com.raquo" %%% "airstream" % "17.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    )
  )

// Modulo examples che dipende da coreJS e view
lazy val examples = project
  .in(file("examples"))
  .dependsOn(coreJS, view)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "17.0.0",
      "com.raquo" %%% "airstream" % "17.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.8.0"
    ),
    scalaJSUseMainModuleInitializer := true,
    Compile / fastOptJS / artifactPath := baseDirectory.value / "target/scala-3.3.3/main.js",
    Compile / mainClass := Some("Main.Main")
  )

// Modulo view che dipende da coreJS
lazy val view = project
  .in(file("view"))
  .dependsOn(coreJS)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "17.0.0",
      "com.raquo" %%% "airstream" % "17.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.8.0"
    )
  )
