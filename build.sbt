lazy val root = (project in file("."))
  .settings(
    name := "kafka-admin-proxy",
    version := "0.0.1",
    scalaVersion := "2.12.4",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
      guice,
      "io.swagger" %% "swagger-play2" % "1.6.0",
      "com.typesafe.play" %% "play-json" % "2.6.9",
      "org.apache.kafka" %% "kafka" % "1.0.1" exclude("org.slf4j", "slf4j-log4j12") exclude("log4j", "log4j"),
      "org.slf4j" % "log4j-over-slf4j" % "1.7.25",
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
    ),
    isSnapshot := false,
    publishTo := {
      val nexus = sys.env.get("NEXUS") match {
        case Some(env) => env
        case None => "http://localhost:8081"
      }
      if (isSnapshot.value)
        Some("snapshots" at nexus + "/repository/maven-snapshots")
      else
       Some("releases" at nexus + "/repository/maven-releases")
    },
    credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
  )
  .enablePlugins(PlayScala)