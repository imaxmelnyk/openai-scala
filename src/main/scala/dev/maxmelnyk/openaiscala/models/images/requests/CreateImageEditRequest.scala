package dev.maxmelnyk.openaiscala.models.images.requests

import dev.maxmelnyk.openaiscala.models.images.ImageSettings

import java.io.File

/**
 * [[https://platform.openai.com/docs/api-reference/images/create-edit]]
 *
 * @param image The image to edit. Must be a valid PNG file, less than 4MB, and square.
 *              If mask is not provided, image must have transparency, which will be used as the mask.
 * @param mask An additional image whose fully transparent areas (e.g. where alpha is zero) indicate where [[image]] should be edited.
 *             Must be a valid PNG file, less than 4MB, and have the same dimensions as [[image]].
 * @param prompt A text description of the desired image(s). The maximum length is 1000 characters.
 * @param settings The settings to use for editing images.
 */
private[openaiscala] case class CreateImageEditRequest(image: File,
                                                       mask: Option[File],
                                                       prompt: String,
                                                       settings: ImageSettings)
