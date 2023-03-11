package dev.maxmelnyk.openaiscala.utils

import io.circe.Decoder

import java.time.{LocalDateTime, ZoneOffset}

private[utils] trait CommonJsonImplicits {
  implicit val localDateTimeDecoder: Decoder[LocalDateTime] = Decoder.decodeLong.map { epochSecond =>
    LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC)
  }
}
