package butte.proxy.data

import org.joda.time.DateTime

case class Log(date: DateTime, client: String, message: Message, opResult: Option[Result] = None)
