package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.settings.{CreateChatCompletionSettings, CreateCompletionSettings}
import dev.maxmelnyk.openaiscala.models.{ChatCompletion, Completion, ModelInfo}
import io.circe.{Decoder, Encoder}
import io.circe.derivation.{Configuration, ConfiguredDecoder, ConfiguredEncoder}

private[openaiscala] object JsonImplicits extends CommonJsonImplicits {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  // models
  implicit val modelPermissionDecoder: Decoder[ModelInfo.Permission] = ConfiguredDecoder.derived
  implicit val modelDecoder: Decoder[ModelInfo] = ConfiguredDecoder.derived

  // completions
  implicit val completionChoiceLogprobsDecoder: Decoder[Completion.Choice.Logprobs] = ConfiguredDecoder.derived
  implicit val completionChoiceDecoder: Decoder[Completion.Choice] = ConfiguredDecoder.derived
  implicit val completionUsageDecoder: Decoder[Completion.Usage] = ConfiguredDecoder.derived
  implicit val completionDecoder: Decoder[Completion] = ConfiguredDecoder.derived

  implicit val createCompletionSettingsEncoder: Encoder[CreateCompletionSettings] = ConfiguredEncoder.derived

  // chat completions
  implicit val chatCompletionMessageEncoder: Encoder[ChatCompletion.Message] = ConfiguredEncoder.derived
  implicit val chatCompletionMessageDecoder: Decoder[ChatCompletion.Message] = ConfiguredDecoder.derived

  implicit val chatCompletionChoiceDecoder: Decoder[ChatCompletion.Choice] = ConfiguredDecoder.derived
  implicit val chatCompletionUsageDecoder: Decoder[ChatCompletion.Usage] = ConfiguredDecoder.derived
  implicit val chatCompletionDecoder: Decoder[ChatCompletion] = ConfiguredDecoder.derived

  implicit val createChatCompletionSettingsEncoder: Encoder[CreateChatCompletionSettings] = ConfiguredEncoder.derived
}
