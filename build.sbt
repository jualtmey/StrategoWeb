name := """StrategoWeb"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.pac4j" % "play-pac4j" % "2.6.1",
  "org.pac4j" % "pac4j-oidc" % "1.9.4" exclude("commons-io" , "commons-io"),
  "commons-io" % "commons-io" % "2.4"
)


fork in run := true

includeFilter in (Assets, LessKeys.less) := "strategoWui.less" | "strategoIndex.less"

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true