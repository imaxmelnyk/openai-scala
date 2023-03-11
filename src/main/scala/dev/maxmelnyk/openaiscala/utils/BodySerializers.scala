package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.settings.CreateCompletionSettings
import dev.maxmelnyk.openaiscala.utils.JsonImplicits._
import io.circe.syntax._
import sttp.client3.{BodySerializer, StringBody}
import sttp.model.MediaType

import java.nio.charset.StandardCharsets


private[openaiscala] object BodySerializers {
  implicit val createCompletionBodySerializer: BodySerializer[(Seq[String], CreateCompletionSettings)] = {
    case (prompts: Seq[String], settings: CreateCompletionSettings) =>
      val bodyJsonString: String = settings
        .asJson
        .deepMerge(Map("prompt" -> prompts).asJson)
        .dropNullValues
        .noSpaces

      StringBody(bodyJsonString, StandardCharsets.UTF_8.toString, MediaType.ApplicationJson)
  }
}
