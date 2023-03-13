package dev.maxmelnyk.openaiscala.utils

import cats.syntax.functor.toFunctorOps
import dev.maxmelnyk.openaiscala.models.images.{Image, ImageSettings}
import dev.maxmelnyk.openaiscala.models.models.Model
import dev.maxmelnyk.openaiscala.models.text.completions.chat.{ChatCompletion, ChatCompletionSettings}
import dev.maxmelnyk.openaiscala.models.text.completions.{Completion, CompletionSettings}
import dev.maxmelnyk.openaiscala.models.text.edits.{Edit, EditSettings}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}
import io.circe.{Decoder, Encoder}

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

  implicit val completionSettingsEncoder: Encoder[CompletionSettings] = deriveConfiguredEncoder

  // chat completions
  implicit val chatCompletionMessageEncoder: Encoder[ChatCompletion.Message] = deriveConfiguredEncoder
  implicit val chatCompletionMessageDecoder: Decoder[ChatCompletion.Message] = deriveConfiguredDecoder

  implicit val chatCompletionChoiceDecoder: Decoder[ChatCompletion.Choice] = deriveConfiguredDecoder
  implicit val chatCompletionUsageDecoder: Decoder[ChatCompletion.Usage] = deriveConfiguredDecoder
  implicit val chatCompletionDecoder: Decoder[ChatCompletion] = deriveConfiguredDecoder

  implicit val chatCompletionSettingsEncoder: Encoder[ChatCompletionSettings] = deriveConfiguredEncoder

  // edits
  implicit val editChoiceDecoder: Decoder[Edit.Choice] = deriveConfiguredDecoder
  implicit val editUsageDecoder: Decoder[Edit.Usage] = deriveConfiguredDecoder
  implicit val editDecoder: Decoder[Edit] = deriveConfiguredDecoder

  implicit val editSettingsEncoder: Encoder[EditSettings] = deriveConfiguredEncoder

  // images
  implicit val imageDataDecoder: Decoder[Image.ImageData] = {
    // should contain all implementations of the base class
    val decoders: List[Decoder[Image.ImageData]] = List(
      deriveConfiguredDecoder[Image.UrlImageData].widen,
      deriveConfiguredDecoder[Image.Base64JsonImageData].widen)

    decoders.reduceLeft(_ or _)
  }

  implicit val imageDecoder: Decoder[Image] = deriveConfiguredDecoder

  implicit val imageSettingsEncoder: Encoder[ImageSettings] = deriveConfiguredEncoder
}
