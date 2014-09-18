package butte.proxy

import akka.actor.Actor
import butte.proxy.data.Message
import spray.routing.HttpService
import butte.proxy.data.MessageJsonSupport._

class ProxyServiceActor extends Actor with ProxyService {
  def actorRefFactory = context

  def receive = runRoute(proxyRoute)
}

trait ProxyService extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher

  val proxyRoute = {
    path("publish") {
      post {
        entity(as[Message]) { message =>
          complete(s"Success - $message")
        }
      }
    }
  }
}
