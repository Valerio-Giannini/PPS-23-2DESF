
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

// Test Dependencies
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"

libraryDependencies += "com.raquo" %%% "laminar" % "17.0.0"
libraryDependencies += "com.raquo" %%% "airstream" % "17.0.0"
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0"

// Quality Assurance
wartremoverWarnings ++= Warts.all

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "2DESF",
    scalaJSUseMainModuleInitializer := true,
  )

// Sources Directories
Compile / unmanagedSourceDirectories += baseDirectory.value / "src/main/scala"

// Test Directories
Test / unmanagedSourceDirectories += baseDirectory.value / "test/main/scala"

