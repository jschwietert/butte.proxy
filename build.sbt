name := "butte-proxy"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.2"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.2"

libraryDependencies += "io.spray" %% "spray-can" % "1.3.1"

libraryDependencies += "io.spray" %% "spray-routing" % "1.3.1"

libraryDependencies += "io.spray" %% "spray-httpx" % "1.3.1"

libraryDependencies += "io.spray" %% "spray-client" % "1.3.1"

libraryDependencies += "io.spray" %% "spray-json" % "1.2.6"
