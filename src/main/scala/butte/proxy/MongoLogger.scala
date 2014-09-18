package butte.proxy

import akka.actor.Actor
import butte.proxy.data.{ Log, Message }
import reactivemongo.api.DefaultDB
import data.BsonFormats._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext

class MongoLogger(db: DefaultDB) extends Actor {
  implicit val ec: ExecutionContext = context.system.dispatcher

  val collection = db[BSONCollection]("log")

  def receive = {
    // Insert
    case l: Log if l.opResult.isEmpty =>
      println(l)

      collection.insert(l) onFailure {
        case t => println(s"Failed to insert log in mongo: $l  Error: $t")
      }

    // Update
    case l: Log =>
      println(l)

      val selector = BSONDocument("date" -> l.date.getMillis)
      collection.update(selector, l) onFailure {
        case t => println(s"Failed to update log in mongo: $l  Error: $t")
      }
  }
}
