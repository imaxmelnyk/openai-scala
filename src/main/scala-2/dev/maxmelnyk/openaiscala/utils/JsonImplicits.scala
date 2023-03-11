package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.settings.CreateCompletionSettings
import dev.maxmelnyk.openaiscala.models.{Completion, ModelInfo}
import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}

private[openaiscala] object JsonImplicits extends CommonJsonImplicits {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val modelPermissionDecoder: Decoder[ModelInfo.Permission] = deriveConfiguredDecoder
  implicit val modelDecoder: Decoder[ModelInfo] = deriveConfiguredDecoder

  implicit val completionChoiceLogprobsDecoder: Decoder[Completion.Choice.Logprobs] = deriveConfiguredDecoder
  implicit val completionChoiceDecoder: Decoder[Completion.Choice] = deriveConfiguredDecoder
  implicit val completionUsageDecoder: Decoder[Completion.Usage] = deriveConfiguredDecoder
  implicit val completionDecoder: Decoder[Completion] = deriveConfiguredDecoder

  implicit val createCompletionSettingsEncoder: Encoder[CreateCompletionSettings] = deriveConfiguredEncoder
}
