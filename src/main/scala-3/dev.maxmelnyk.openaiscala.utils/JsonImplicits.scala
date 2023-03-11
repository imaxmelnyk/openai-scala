package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.settings.CreateCompletionSettings
import dev.maxmelnyk.openaiscala.models.{Completion, ModelInfo}
import io.circe.{Decoder, Encoder}
import io.circe.derivation.{Configuration, ConfiguredDecoder, ConfiguredEncoder}

private[openaiscala] object JsonImplicits extends CommonJsonImplicits {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val modelPermissionDecoder: Decoder[ModelInfo.Permission] = ConfiguredDecoder.derived
  implicit val modelDecoder: Decoder[ModelInfo] = ConfiguredDecoder.derived

  implicit val completionChoiceLogprobsDecoder: Decoder[Completion.Choice.Logprobs] = ConfiguredDecoder.derived
  implicit val completionChoiceDecoder: Decoder[Completion.Choice] = ConfiguredDecoder.derived
  implicit val completionUsageDecoder: Decoder[Completion.Usage] = ConfiguredDecoder.derived
  implicit val completionDecoder: Decoder[Completion] = ConfiguredDecoder.derived

  implicit val createCompletionSettingsEncoder: Encoder[CreateCompletionSettings] = ConfiguredEncoder.derived
}
