package butte.proxy

import akka.actor.Actor

class StdoutLogger extends Actor {
  def receive = {
    case msg => println(msg)
  }
}
