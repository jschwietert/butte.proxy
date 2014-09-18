package butte.proxy

import akka.actor.Actor
import butte.proxy.data.{ Log, Message }
import reactivemongo.api.DefaultDB
import data.JsonFormats._

class MongoLogger(db: DefaultDB) extends Actor {
  val collection = db.collection("log")

  def receive = {
    // Insert
    case l: Log if l.opResult.isEmpty =>
//      collection.insert()

    // Update
    case l: Log =>
//      collection.update(Json.obj("lastName" -> lastName))
  }
}
