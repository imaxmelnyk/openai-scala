package dev.maxmelnyk.openaiscala.models.images

import enumeratum._

/**
 * [[https://platform.openai.com/docs/api-reference/images]]
 *
 * @param n The number of images to generate. Must be between 1 and 10.
 * @param size The size of the generated images.
 * @param responseFormat The format in which the generated images are returned.
 * @param user A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
 */
case class ImageSettings(n: Option[Long] = None,
                         size: Option[ImageSettings.Size] = None,
                         responseFormat: Option[ImageSettings.ResponseFormat] = None,
                         user: Option[String] = None)

object ImageSettings {
  sealed abstract class Size(override val entryName: String) extends EnumEntry
  object Size extends Enum[Size] {
    case object Square256 extends Size("256x256")
    case object Square512 extends Size("512x512")
    case object Square1024 extends Size("1024x1024")

    val values = findValues
  }

  sealed abstract class ResponseFormat(override val entryName: String) extends EnumEntry
  object ResponseFormat extends Enum[ResponseFormat] {
    case object Url extends ResponseFormat("url")
    case object Base64Json extends ResponseFormat("b64_json")

    val values = findValues
  }
}
