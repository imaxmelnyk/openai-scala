package dev.maxmelnyk.openaiscala.models.text.edits.requests

import dev.maxmelnyk.openaiscala.models.text.edits.EditSettings

/**
 * [[https://platform.openai.com/docs/api-reference/edits/create]]
 *
 * @param input The input text to use as a starting point for the edit.
 * @param instruction The instruction that tells the model how to edit the prompt.
 * @param settings The settings to use for editing text.
 */
private[openaiscala] case class CreateEditRequest(input: String,
                                                  instruction: String,
                                                  settings: EditSettings)
