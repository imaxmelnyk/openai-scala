package dev.maxmelnyk.openaiscala.utils

import cats.syntax.functor.toFunctorOps
import dev.maxmelnyk.openaiscala.models.images.{Image, ImageSettings}
import dev.maxmelnyk.openaiscala.models.models.Model
import dev.maxmelnyk.openaiscala.models.text.completions.chat.{ChatCompletion, ChatCompletionSettings}
import dev.maxmelnyk.openaiscala.models.text.completions.{Completion, CompletionSettings}
import dev.maxmelnyk.openaiscala.models.text.edits.{Edit, EditSettings}
import io.circe.{Decoder, Encoder}
import io.circe.derivation.{Configuration, ConfiguredDecoder, ConfiguredEncoder}

private[openaiscala] object JsonImplicits extends CommonJsonImplicits {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  // models
  implicit val modelPermissionDecoder: Decoder[Model.Permission] = ConfiguredDecoder.derived
  implicit val modelDecoder: Decoder[Model] = ConfiguredDecoder.derived

  // completions
  implicit val completionChoiceLogprobsDecoder: Decoder[Completion.Choice.Logprobs] = ConfiguredDecoder.derived
  implicit val completionChoiceDecoder: Decoder[Completion.Choice] = ConfiguredDecoder.derived
  implicit val completionUsageDecoder: Decoder[Completion.Usage] = ConfiguredDecoder.derived
  implicit val completionDecoder: Decoder[Completion] = ConfiguredDecoder.derived

  implicit val completionSettingsEncoder: Encoder[CompletionSettings] = ConfiguredEncoder.derived

  // chat completions
  implicit val chatCompletionMessageEncoder: Encoder[ChatCompletion.Message] = ConfiguredEncoder.derived
  implicit val chatCompletionMessageDecoder: Decoder[ChatCompletion.Message] = ConfiguredDecoder.derived

  implicit val chatCompletionChoiceDecoder: Decoder[ChatCompletion.Choice] = ConfiguredDecoder.derived
  implicit val chatCompletionUsageDecoder: Decoder[ChatCompletion.Usage] = ConfiguredDecoder.derived
  implicit val chatCompletionDecoder: Decoder[ChatCompletion] = ConfiguredDecoder.derived

  implicit val chatCompletionSettingsEncoder: Encoder[ChatCompletionSettings] = ConfiguredEncoder.derived

  // edits
  implicit val editChoiceDecoder: Decoder[Edit.Choice] = ConfiguredDecoder.derived
  implicit val editUsageDecoder: Decoder[Edit.Usage] = ConfiguredDecoder.derived
  implicit val editDecoder: Decoder[Edit] = ConfiguredDecoder.derived

  implicit val editSettingsEncoder: Encoder[EditSettings] = ConfiguredEncoder.derived

  // images
  implicit val imageSettingsEncoder: Encoder[ImageSettings] = ConfiguredEncoder.derived

  implicit val imageDataDecoder: Decoder[Image.ImageData] = {
    val urlImageDataDecoder: Decoder[Image.UrlImageData] = ConfiguredDecoder.derived
    val base64JsonImageDataDecoder: Decoder[Image.Base64JsonImageData] = ConfiguredDecoder.derived

    // should contain all implementations of the base class
    val decoders: List[Decoder[Image.ImageData]] = List(
      urlImageDataDecoder.widen,
      base64JsonImageDataDecoder.widen)

    decoders.reduceLeft(_ or _)
  }

  implicit val imageDecoder: Decoder[Image] = ConfiguredDecoder.derived
}
