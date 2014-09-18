package butte.proxy

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

object Runner extends App {
  implicit val system = ActorSystem("butte-proxy")

  val service = system.actorOf(Props[ProxyServiceActor], "proxy-service")

  IO(Http) ! Http.Bind(service, "0.0.0.0", port = 8080)
}
