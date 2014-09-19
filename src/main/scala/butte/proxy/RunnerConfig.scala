package butte.proxy

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

trait RunnerConfig {
  private lazy val conf = ConfigFactory.load().getConfig("butte").getConfig("proxy")

  protected final lazy val bindIp = conf.getString("bind-ip")
  protected final lazy val bindPort = conf.getInt("bind-port")
  protected final lazy val mongoHosts = conf.getStringList("mongo-hosts").asScala
}
