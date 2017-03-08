
val scalaV = "2.12.1"

lazy val commonSettings = Seq(
    organization := "com.example",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scalaV,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint:_,-missing-interpolator"),
    resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)
)

lazy val root = (project in file("."))
    .settings(commonSettings: _*)
    .dependsOn(macros)

lazy val macros = (project in file("macros")).settings(
    name := "macros",
    libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-reflect" % scalaV
    )
).settings(commonSettings: _*)
