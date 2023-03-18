package dev.maxmelnyk.openaiscala.models.images.requests

import dev.maxmelnyk.openaiscala.models.images.ImageSettings

import java.io.File

/**
 * [[https://platform.openai.com/docs/api-reference/images/create-variation]]
 *
 * @param image The image to use as the basis for the variation(s).
 *              Must be a valid PNG file, less than 4MB, and square.
 * @param settings The settings to use for creating image variations.
 */
private[openaiscala] case class CreateImageVariationRequest(image: File,
                                                            settings: ImageSettings)
