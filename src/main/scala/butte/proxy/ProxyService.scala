package butte.proxy

import java.util.concurrent.TimeUnit

import akka.actor.{ Actor, ActorRef }
import akka.pattern.ask
import akka.util.Timeout
import butte.proxy.data.{ Result, Log, Message }
import butte.proxy.data.MessageJsonSupport._
import org.joda.time.{ DateTimeZone, DateTime }
import spray.http.{ RemoteAddress, HttpResponse, StatusCodes }
import spray.routing.HttpService

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class ProxyServiceActor(logger: ActorRef, twitterClient: ActorRef) extends Actor with ProxyService {
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

  def postToTwitter(clientIp: RemoteAddress, message: Message): Future[HttpResponse] = {
    val log = Log(DateTime.now(DateTimeZone.UTC), clientIp.toString, message)
    logger ! log

    val f = twitterClient ? message
    f.mapTo[TwitterPostResult] map {
      case TwitterPostSuccess =>
        logger ! log.copy(opResult = Some(Result("Success")))
        HttpResponse(StatusCodes.OK, "OK")

      case TwitterPostFailure(msg) =>
        logger ! log.copy(opResult = Some(Result(s"Failure: $msg")))
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
          clientIP { ip =>
            complete {
              postToTwitter(ip, message)
            }
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

  def postToTwitter(clientIp: RemoteAddress, message: Message): Future[HttpResponse]

  def stopAfter(delay: FiniteDuration): Unit
}
