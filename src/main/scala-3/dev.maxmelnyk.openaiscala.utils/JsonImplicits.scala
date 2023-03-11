package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.ModelInfo
import io.circe.Decoder
import io.circe.derivation.{Configuration, ConfiguredDecoder}

private[openaiscala] object JsonImplicits extends CommonJsonImplicits {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val modelPermissionDecoder: Decoder[ModelInfo.Permission] = ConfiguredDecoder.derived
  implicit val modelDecoder: Decoder[ModelInfo] = ConfiguredDecoder.derived
}
