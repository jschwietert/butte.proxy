package butte.proxy

import akka.actor.Actor
import butte.proxy.data.Message
import butte.proxy.ext.twitter.TwitterAuthorization
import butte.proxy.ext.twitter.oauth.OAuthTwitterAuthorization

class TwitterClient extends Actor {
  private val auth: TwitterAuthorization = new OAuthTwitterAuthorization {}

  def receive = {
    case Message(body) =>
    // TODO: Post to Twitter
  }

}
