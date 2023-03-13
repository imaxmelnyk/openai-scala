package dev.maxmelnyk.openaiscala.models.images

import java.time.LocalDateTime

/**
 * [[https://platform.openai.com/docs/api-reference/images]]
 */
case class Image(created: LocalDateTime,
                 data: Seq[Image.ImageData])

object Image {
  sealed trait ImageData
  case class UrlImageData(url: String) extends ImageData
  case class Base64JsonImageData(b64Json: String) extends ImageData
}
