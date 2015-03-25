
name := "ocean"

organization in ThisBuild  := "ch.usi.itc"

version in ThisBuild := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild  := "2.11.2"


resolvers in ThisBuild += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

// OceanProjects method to create the common project structure of all sub-module
def oceanProjects(name:String): Project = {
  Project(name, file(name)).settings(
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.1.1" % "test",
    libraryDependencies += "org.pegdown" % "pegdown" % "1.4.2" % "test",
    libraryDependencies += "com.softwaremill.macwire" %% "macros" % "0.7",
    libraryDependencies += "com.softwaremill.macwire" %% "runtime" % "0.7",
  resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
  )
}


lazy val oceanmessaging = oceanProjects("ocean-messaging").
  settings(
    libraryDependencies ++= Seq( "commons-lang" % "commons-lang" % "2.6",
      "org.apache.cxf" % "cxf-rt-frontend-jaxws" % "3.0.1",
      "org.apache.cxf" % "cxf-rt-transports-http" % "3.0.1",
      "org.apache.cxf" % "cxf-rt-transports-http-jetty" % "3.0.1",
      "org.apache.cxf" % "cxf-rt-transports-http-hc" % "3.0.1",
      "com.sun.xml.bind" % "jaxb-jxc" % "2.2.10-b140310.1920",
      "org.apache.cxf.xjc-utils" % "cxf-xjc-runtime" % "3.0.1")
  )


lazy val oceanois = oceanProjects("ocean-ois").settings(
  libraryDependencies ++= Seq("org.apache.jena" % "jena-core" % "2.11.0",
    "org.apache.jena" % "jena-tdb" % "1.0.0",
    "org.apache.jena" % "jena-iri" % "1.0.0",
    "org.apache.jena" % "jena-arq" % "2.11.0",
    "joda-time" % "joda-time" % "2.3",
    "org.joda" % "joda-convert" % "1.5",
    "com.google.inject" % "guice" % "4.0-beta4",
    "com.google.inject.extensions" % "guice-assistedinject" % "4.0-beta4",
    "com.google.inject.extensions" % "guice-multibindings" % "4.0-beta4",
    "net.codingwell" % "scala-guice_2.11" % "4.0.0-beta4"
    )
  ).dependsOn(oceanmessaging).dependsOn(oceanpelletswrlbuiltins).
  dependsOn(commoncontentLang).dependsOn(commoncontentLangcodec)


lazy val oceanoisGui = oceanProjects("ocean-ois-gui").dependsOn(oceanois).settings(
  unmanagedJars in Compile += Attributed.blank(file(scala.util.Properties.javaHome) / "/lib/jfxrt.jar")
)

lazy val oceanoisApp = oceanProjects("ocean-ois-app").dependsOn(oceanoisGui).dependsOn(oceanois)

//TODO Change the Organization ID of pellet dependencies to ch.usi.itc. The goal is to have your pellet local that depends on jena 2.11.0 published online and accessible to everyone that decide to compile with your project. The original depend on 2.10.1. However if you decide to depend on the official pellet version that you will have to exclude the transitive dependency to jena 2.10.1
lazy val oceanpelletswrlbuiltins = oceanProjects("ocean-pellet-swrlbuiltins").
  settings(
    resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    libraryDependencies += "joda-time" % "joda-time" % "2.3",
    libraryDependencies += "org.joda" % "joda-convert" % "1.5",
    libraryDependencies ++= Seq("com.clarkparsia.pellet" % "pellet-core" % "2.3.2-SNAPSHOT",
    "com.clarkparsia.pellet" % "pellet-explanation" % "2.3.2-SNAPSHOT",
    "com.clarkparsia.pellet" % "pellet-modularity" % "2.3.2-SNAPSHOT",
    "com.clarkparsia.pellet" % "pellet-pellint" % "2.3.2-SNAPSHOT",
    "com.clarkparsia.pellet" % "pellet-query" % "2.3.2-SNAPSHOT",
    "com.clarkparsia.pellet" % "pellet-owlapiv3" % "2.3.2-SNAPSHOT")
  )



lazy val commoncontentLang = oceanProjects("common-contentlang").
  settings(libraryDependencies += "commons-codec" % "commons-codec" % "1.7").dependsOn(oceanmessaging)

lazy val commonoceanOnto = oceanProjects("common-ocean-ontologies").dependsOn(commoncontentLang)

lazy val commoncontentLangcodec = oceanProjects("common-contentlang-codec").
  dependsOn(commoncontentLang).dependsOn(commonoceanOnto).
  settings(libraryDependencies ++=Seq("org.apache.jena" % "jena-core" % "2.11.0",
    "joda-time" % "joda-time" % "2.3",
    "org.joda" % "joda-convert" % "1.5"))

