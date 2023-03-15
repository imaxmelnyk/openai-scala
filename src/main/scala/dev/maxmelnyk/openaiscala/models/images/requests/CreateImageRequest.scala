package dev.maxmelnyk.openaiscala.models.images.requests

import dev.maxmelnyk.openaiscala.models.images.ImageSettings

/**
 * https://platform.openai.com/docs/api-reference/images/create
 *
 * @param prompt A text description of the desired image(s). The maximum length is 1000 characters.
 * @param settings The settings to use for creating images.
 */
private[openaiscala] case class CreateImageRequest(prompt: String,
                                                   settings: ImageSettings)
