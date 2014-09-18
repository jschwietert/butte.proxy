package butte.proxy.data

import org.joda.time.{ DateTime, DateTimeZone }
import reactivemongo.bson._

object BsonFormats {

  implicit object PersonReader extends BSONDocumentReader[Log] {
    def read(doc: BSONDocument): Log = {
      val date = new DateTime(doc.getAs[Long]("date").get, DateTimeZone.UTC)
      val client = doc.getAs[String]("client").get
      val message = doc.getAs[String]("message").map(Message(_)).get
      val opResult = doc.getAs[String]("opResult").map(Result(_))

      Log(date, client, message, opResult)
    }
  }
  implicit object PersonWriter extends BSONDocumentWriter[Log] {
    def write(log: Log): BSONDocument = {
      val doc = BSONDocument(
        "date" -> log.date.getMillis,
        "client" -> log.client,
        "message" -> log.message.body) // TODO - send the entire message

      log.opResult match {
        case None => doc
        case Some(result) => doc.add("opResult" -> result.message)
      }
    }
  }
}
