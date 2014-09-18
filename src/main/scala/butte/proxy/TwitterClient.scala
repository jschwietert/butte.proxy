package butte.proxy

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import butte.proxy.data.Message
import butte.proxy.ext.twitter.TwitterAuthorization
import butte.proxy.ext.twitter.oauth.OAuthTwitterAuthorization
import spray.can.Http
import spray.http._

import scala.concurrent.ExecutionContext

sealed abstract class TwitterPostResult(val successful: Boolean)

case object TwitterPostSuccess extends TwitterPostResult(true)

case class TwitterPostFailure(message: String) extends TwitterPostResult(false)

class TwitterClient extends Actor {
  private val auth: TwitterAuthorization = new OAuthTwitterAuthorization {}

  private implicit val timeout = Timeout.apply(5, TimeUnit.SECONDS)

  private implicit val system = context.system

  private implicit val ec: ExecutionContext = system.dispatcher

  def receive = {
    case Message(body) =>
      val f = IO(Http).ask(postRequest(body)).mapTo[HttpResponse]
      val thisSender = sender()
      f foreach { response =>
        response.status match {
          case StatusCodes.OK =>
            thisSender ! TwitterPostSuccess
          case _ =>
            thisSender ! TwitterPostFailure(response.entity.asString)
        }
      }
      f onFailure {
        case error =>
          thisSender ! TwitterPostFailure(error.toString)
      }
  }

  private def postRequest(message: String): HttpRequest = {
    val formData = FormData(Map("status" -> message))
    val request = spray.client.pipelining.Post("https://api.twitter.com/1.1/statuses/update.json", formData)
    auth.authorize(request)
  }

}
