package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.ChatCompletion
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
}
