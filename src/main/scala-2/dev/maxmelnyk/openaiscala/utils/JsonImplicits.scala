package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.settings._
import dev.maxmelnyk.openaiscala.models._
import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}

private[openaiscala] object JsonImplicits extends CommonJsonImplicits {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  // models
  implicit val modelPermissionDecoder: Decoder[Model.Permission] = deriveConfiguredDecoder
  implicit val modelDecoder: Decoder[Model] = deriveConfiguredDecoder

  // completions
  implicit val completionChoiceLogprobsDecoder: Decoder[Completion.Choice.Logprobs] = deriveConfiguredDecoder
  implicit val completionChoiceDecoder: Decoder[Completion.Choice] = deriveConfiguredDecoder
  implicit val completionUsageDecoder: Decoder[Completion.Usage] = deriveConfiguredDecoder
  implicit val completionDecoder: Decoder[Completion] = deriveConfiguredDecoder

  implicit val createCompletionSettingsEncoder: Encoder[CreateCompletionSettings] = deriveConfiguredEncoder

  // chat completions
  implicit val chatCompletionMessageEncoder: Encoder[ChatCompletion.Message] = deriveConfiguredEncoder
  implicit val chatCompletionMessageDecoder: Decoder[ChatCompletion.Message] = deriveConfiguredDecoder

  implicit val chatCompletionChoiceDecoder: Decoder[ChatCompletion.Choice] = deriveConfiguredDecoder
  implicit val chatCompletionUsageDecoder: Decoder[ChatCompletion.Usage] = deriveConfiguredDecoder
  implicit val chatCompletionDecoder: Decoder[ChatCompletion] = deriveConfiguredDecoder

  implicit val createChatCompletionSettingsEncoder: Encoder[CreateChatCompletionSettings] = deriveConfiguredEncoder

  // edits
  implicit val editChoiceDecoder: Decoder[Edit.Choice] = deriveConfiguredDecoder
  implicit val editUsageDecoder: Decoder[Edit.Usage] = deriveConfiguredDecoder
  implicit val editDecoder: Decoder[Edit] = deriveConfiguredDecoder

  implicit val createEditSettingsEncoder: Encoder[CreateEditSettings] = deriveConfiguredEncoder
}
