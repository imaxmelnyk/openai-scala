package dev.maxmelnyk.openaiscala.models.text.completions.requests

import dev.maxmelnyk.openaiscala.models.text.completions.CompletionSettings

/**
 * [[https://platform.openai.com/docs/api-reference/completions/create]]
 *
 * @param prompts The prompts to generate completions for.
                  Note that `<|endoftext|>` is the document separator that the model sees during training,
                  so if a prompts is not specified the model will generate as if from the beginning of a new document.
 * @param settings The settings to use for completing text.
 */
private[openaiscala] case class CreateCompletionRequest(prompts: Seq[String],
                                                        settings: CompletionSettings)
