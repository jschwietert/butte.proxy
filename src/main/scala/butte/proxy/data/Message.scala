package butte.proxy.data

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Message(body: String)

object MessageJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val PortfolioFormats = jsonFormat1(Message)
}
