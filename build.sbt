name := "memes2"
 
version := "1.0" 
      
lazy val `memes2` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( javaJdbc , guice , javaWs , "org.mindrot" % "jbcrypt" % "0.3m")

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

      