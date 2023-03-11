package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.models.ModelInfo
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder

private[openaiscala] object JsonImplicits extends CommonJsonImplicits {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val modelPermissionDecoder: Decoder[ModelInfo.Permission] = deriveConfiguredDecoder
  implicit val modelDecoder: Decoder[ModelInfo] = deriveConfiguredDecoder
}
