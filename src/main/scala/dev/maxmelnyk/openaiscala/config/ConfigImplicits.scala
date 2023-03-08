package dev.maxmelnyk.openaiscala.config

import com.typesafe.config.Config

private[config] object ConfigImplicits {
  implicit class ConfigExt(config: Config) {
    private def getOptional[T](path: String, resolver: String => T): Option[T] = {
      if (config.hasPath(path)) Some(resolver(path)) else None
    }

    def getOptionalString(path: String): Option[String] = {
      getOptional(path, config.getString)
    }
  }
}
