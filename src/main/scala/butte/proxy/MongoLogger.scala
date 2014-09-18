package butte.proxy

import akka.actor.Actor
import butte.proxy.data.{ Log, Message }
import reactivemongo.api.DefaultDB
import data.BsonFormats._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument

class MongoLogger(db: DefaultDB) extends Actor {
  val collection = db[BSONCollection]("log")

  def receive = {
    // Insert
    case l: Log if l.opResult.isEmpty =>
      collection.insert(l)

    // Update
    case l: Log =>
      val selector = BSONDocument("date" -> l.date.getMillis)
      collection.update(selector, l)
  }
}
