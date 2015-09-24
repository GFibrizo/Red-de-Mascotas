name := "Server"

version := "1.0"

lazy val `server` = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaCore,
  javaWs % "test",
  "org.springframework" % "spring-context" % "4.1.1.RELEASE",
  "org.springframework" % "spring-orm" % "4.1.1.RELEASE",
  "org.springframework" % "spring-jdbc" % "4.1.1.RELEASE",
  "org.springframework" % "spring-tx" % "4.1.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.1.1.RELEASE",
  "org.springframework" % "spring-aop" % "4.1.1.RELEASE",
  "org.springframework" % "spring-test" % "4.1.1.RELEASE" % "test",
  "net.vz.mongodb.jackson" % "play-mongo-jackson-mapper_2.10" % "1.1.0"
)
