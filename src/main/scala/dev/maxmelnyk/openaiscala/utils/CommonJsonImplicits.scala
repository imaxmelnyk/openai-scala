package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.images.ImageSettings
import dev.maxmelnyk.openaiscala.models.text.completions.chat.ChatCompletion
import enumeratum.Circe
import io.circe.{Decoder, Encoder}

import java.time.{LocalDateTime, ZoneOffset}

private[utils] trait CommonJsonImplicits {
  implicit val localDateTimeDecoder: Decoder[LocalDateTime] = Decoder.decodeLong.map { epochSecond =>
    LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC)
  }

  // chat completions
  implicit val chatCompletionMessageRoleEncoder: Encoder[ChatCompletion.Message.Role] = Circe.encoder(ChatCompletion.Message.Role)
  implicit val chatCompletionMessageRoleDecoder: Decoder[ChatCompletion.Message.Role] = Circe.decoder(ChatCompletion.Message.Role)

  // images
  implicit val imageSizeEncoder: Encoder[ImageSettings.Size] = Circe.encoder(ImageSettings.Size)
  implicit val imageResponseFormatEncoder: Encoder[ImageSettings.ResponseFormat] = Circe.encoder(ImageSettings.ResponseFormat)
}
