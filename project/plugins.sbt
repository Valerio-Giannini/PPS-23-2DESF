// Formatter
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.16.0")

// sbt-crossproject plugin per supportare progetti multipiattaforma (JVM + JS) poichè il core compila per JVM
//Il core deve essere sfruttato anche dal progetto JS
addSbtPlugin("org.portable-scala" % "sbt-crossproject" % "1.3.2")
