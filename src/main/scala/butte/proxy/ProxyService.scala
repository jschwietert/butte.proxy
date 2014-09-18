package butte.proxy

import java.util.concurrent.TimeUnit

import akka.actor.{ Actor, ActorRef }
import akka.pattern.ask
import akka.util.Timeout
import butte.proxy.data.Message
import butte.proxy.data.MessageJsonSupport._
import spray.http.{ HttpResponse, StatusCodes }
import spray.routing.HttpService

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class ProxyServiceActor(twitterClient: ActorRef) extends Actor with ProxyService {
  implicit val timeout = Timeout.apply(5, TimeUnit.SECONDS)

  implicit def system = actorRefFactory.system

  def actorRefFactory = context

  def receive = runRoute(proxyRoute)

  def test: Future[HttpResponse] = {
    val f = twitterClient ? Message("This is a test posted at " + new java.util.Date())
    f.mapTo[TwitterPostResult] map {
      case TwitterPostSuccess =>
        HttpResponse(StatusCodes.OK, "OK")
      case TwitterPostFailure(msg) =>
        HttpResponse(StatusCodes.InternalServerError, msg)
    }
  }

  def postToTwitter(message: Message): Future[HttpResponse] = {
    val f = twitterClient ? message
    f.mapTo[TwitterPostResult] map {
      case TwitterPostSuccess =>
        HttpResponse(StatusCodes.OK, "OK")
      case TwitterPostFailure(msg) =>
        HttpResponse(StatusCodes.InternalServerError, msg)
    }
  }

  def stopAfter(delay: FiniteDuration): Unit =
    system.scheduler.scheduleOnce(delay) {
      system.shutdown()
    }

}

trait ProxyService extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher

  val proxyRoute = {
    path("publish") {
      post {
        entity(as[Message]) { message =>
          complete {
            postToTwitter(message)
          }
        }
      }
    } ~
      path("test") {
        dynamic {
          get {
            complete {
              test
            }
          }
        }
      } ~
      path("stop") {
        get {
          complete {
            stopAfter(FiniteDuration(1, TimeUnit.SECONDS))
            "Shutting down in 1 second."
          }
        }
      }
  }

  def test: Future[HttpResponse]

  def postToTwitter(message: Message): Future[HttpResponse]

  def stopAfter(delay: FiniteDuration): Unit
}
