package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.Model
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import java.time.{LocalDateTime, ZoneOffset}

private[openaiscala] object JsonImplicits {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val localDateTimeDecoder: Decoder[LocalDateTime] = Decoder.decodeLong.map { epochSecond =>
    LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC)
  }

  implicit val modelPermissionDecoder: Decoder[Model.Permission] = deriveConfiguredDecoder
  implicit val modelDecoder: Decoder[Model] = deriveConfiguredDecoder
}
