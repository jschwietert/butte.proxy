name := "butte-proxy"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.2"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.2"

libraryDependencies += "io.spray" %% "spray-can" % "1.3.1"

libraryDependencies += "io.spray" %% "spray-routing" % "1.3.1"

libraryDependencies += "io.spray" %% "spray-httpx" % "1.3.1"

libraryDependencies += "io.spray" %% "spray-json" % "1.2.6"

libraryDependencies += "org.reactivemongo" %% "reactivemongo" % "0.10.5.akka23-SNAPSHOT"

libraryDependencies += "joda-time" % "joda-time" % "2.4"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

