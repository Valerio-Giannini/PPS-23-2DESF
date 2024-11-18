/**
 * Adds plugins to enhance the build process for the "2DESF" project.
 *
 * These plugins provide support for code formatting, Scala.js, and benchmarking.
 */

/**
 * Adds the Scalafmt plugin.
 *
 * Scalafmt is a code formatter for Scala, ensuring consistent code style
 * across the project. This plugin integrates Scalafmt into the sbt build process.
 *
 * @dependency "org.scalameta" % "sbt-scalafmt" % "2.5.2"
 */
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

/**
 * Adds the Scala.js plugin.
 *
 * The Scala.js plugin enables the development and compilation of Scala code
 * into JavaScript. This plugin is essential for creating front-end applications
 * and simulations in Scala.js.
 *
 * @dependency "org.scala-js" % "sbt-scalajs" % "1.16.0"
 */
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.16.0")

/**
 * Adds the JMH plugin for benchmarking.
 *
 * The JMH (Java Microbenchmark Harness) plugin facilitates performance benchmarking
 * in Scala projects. It integrates JMH into the sbt build process, making it easier
 * to measure and optimize code performance.
 *
 * @dependency "pl.project13.scala" % "sbt-jmh" % "0.4.7"
 */
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.4.7")