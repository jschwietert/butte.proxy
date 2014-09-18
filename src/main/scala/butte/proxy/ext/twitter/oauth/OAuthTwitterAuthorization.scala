// From: https://github.com/eigengo/activator-spray-twitter/
// License: Apache2 per https://github.com/eigengo/activator-spray-twitter/blob/master/LICENSE

package butte.proxy.ext.twitter.oauth

import butte.proxy.ext.twitter.TwitterAuthorization
import spray.http.HttpRequest

import scala.io.Source

trait OAuthTwitterAuthorization extends TwitterAuthorization {
  import butte.proxy.ext.twitter.oauth.OAuth._
  val home = System.getProperty("user.home")
  val lines = Source.fromFile(s"$home/.twitter/butte-proxy").getLines().toList
  val consumer = Consumer(lines(0), lines(1))
  val token = Token(lines(2), lines(3))
  val authorize: (HttpRequest) => HttpRequest = oAuthAuthorizer(consumer, token)
}
