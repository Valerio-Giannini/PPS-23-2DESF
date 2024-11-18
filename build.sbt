/**
 * Build configuration for the "2DESF" project, defining multiple modules for shared, JVM, and JS-specific code.
 *
 * The build leverages sbt for dependency management and project setup, supporting Scala.js for front-end development
 * and JVM for back-end or testing purposes. The project includes core modules (`core/shared`, `core/jvm`, `core/js`),
 * simulations, and benchmarks.
 */
import sbt.Keys.libraryDependencies
import scala.collection.Seq

/**
 * Sets the global build settings.
 *
 * - `scalaVersion`: Specifies Scala 3.3.3 as the compiler version.
 * - `version`: Sets the project version to "0.1.1".
 * - `name`: Names the project "2DESF".
 */
ThisBuild / scalaVersion := "3.3.3"
ThisBuild / version      := "0.1.1"
ThisBuild / name         := "2DESF"

/**
 * The root project, aggregating all submodules.
 *
 * This project does not contain any direct code but serves as an entry point for managing all other modules.
 */
lazy val root = project
  .in(file("."))
  .aggregate(coreJVM, coreJS, simulations)

/**
 * Core shared module for common code.
 *
 * - Directory: `core/shared`.
 * - Enabled with `ScalaJSPlugin` to support cross-compilation.
 * - Contains common logic shared between JVM and JS platforms.
 */
lazy val core = project
  .in(file("core/shared"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
  )

/**
 * Core JVM-specific module.
 *
 * - Directory: `core/jvm`.
 * - Depends on the shared core module.
 * - Includes dependencies for testing with `Scalactic` and `ScalaTest`.
 */
lazy val coreJVM = project
  .in(file("core/jvm"))
  .dependsOn(core)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.19",
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
    )
  )

/**
 * Core JS-specific module.
 *
 * - Directory: `core/js`.
 * - Depends on the shared core module.
 * - Enabled with `ScalaJSPlugin`.
 * - Includes dependencies for front-end development with Laminar, Airstream, and scalajs-dom.
 */
lazy val coreJS = project
  .in(file("core/js"))
  .dependsOn(core)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "17.0.0",
      "com.raquo" %%% "airstream" % "17.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    )
  )

/**
 * Simulations module for running and managing simulations.
 *
 * - Directory: `Simulations`.
 * - Depends on the `coreJS` module.
 * - Enabled with `ScalaJSPlugin` for JS compilation.
 * - Configures the output of the `fastOptJS` build process to a specific file path.
 */
lazy val simulations = project
  .in(file("Simulations"))
  .dependsOn(coreJS)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "17.0.0",
      "com.raquo" %%% "airstream" % "17.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.8.0"
    ),
    scalaJSUseMainModuleInitializer := true,
    Compile / fastOptJS / artifactPath := baseDirectory.value / "target/scala-3.3.3/main.js",
  )

/**
 * Benchmarks module for performance testing.
 *
 * - Directory: `benchmarks`.
 * - Depends on the shared core module.
 * - Enabled with `JmhPlugin` for benchmarking.
 */
lazy val benchmarks = project
  .in(file("benchmarks"))
  .dependsOn(core)
  .enablePlugins(JmhPlugin)
