package dev.maxmelnyk.openaiscala.config

import com.typesafe.config.ConfigFactory
import dev.maxmelnyk.openaiscala.config.ConfigImplicits._

private [openaiscala] object Config {
  private val config = ConfigFactory.load("openai-scala.conf").resolve()

  val openAiApiKey: String = config.getString("openai-scala.openai.api-key")
  val openAiOrgId: Option[String] = config.getOptionalString("openai-scala.openai.org-id")
}
