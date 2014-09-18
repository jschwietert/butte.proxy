package butte.proxy

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

object Runner extends App {
  implicit val system = ActorSystem("butte-proxy")

  val db = connect()

  val logger = system.actorOf(Props(classOf[MongoLogger], db), "mongo-logger")

  val service = system.actorOf(Props[ProxyServiceActor], "proxy-service")

  IO(Http) ! Http.Bind(service, "0.0.0.0", port = 8080)

  def connect() = {
    import reactivemongo.api._
    import scala.concurrent.ExecutionContext.Implicits.global

    // gets an instance of the driver
    // (creates an actor system)
    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))

    // Gets a reference to the database
    connection("butte-proxy")
  }
}
