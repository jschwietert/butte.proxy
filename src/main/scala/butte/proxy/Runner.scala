package butte.proxy

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

object Runner extends App {
  implicit val system = ActorSystem("butte-proxy")

  val MongoHosts = List("localhost")
  val BindIP = "0.0.0.0"
  val BindPort = 8080

  val db = connect()

  val logger = system.actorOf(Props(classOf[MongoLogger], db), "mongo-logger")
  val service = system.actorOf(Props[ProxyServiceActor], "proxy-service")

  IO(Http) ! Http.Bind(service, BindIP, port = BindPort)

  def connect() = {
    import reactivemongo.api._
    import scala.concurrent.ExecutionContext.Implicits.global

    // gets an instance of the driver
    // (creates an actor system)
    val driver = new MongoDriver
    val connection = driver.connection(MongoHosts)

    // Gets a reference to the database
    connection("butte-proxy")
  }
}
