package dev.maxmelnyk.openaiscala.models.text.edits

import dev.maxmelnyk.openaiscala.models.models.Models

/**
 * [[https://platform.openai.com/docs/api-reference/edits]]
 *
 * @param model ID of the model to use.
 * @param n How many edits to generate for the input and instruction.
 * @param temperature What sampling temperature to use, between 0 and 2.
 *                    Higher values like 0.8 will make the output more random,
 *                    while lower values like 0.2 will make it more focused and deterministic.
 *                    We generally recommend altering this or [[topP]] but not both.
 * @param topP An alternative to sampling with temperature, called nucleus sampling,
 *             where the model considers the results of the tokens with [[topP]] probability mass.
 *             So 0.1 means only the tokens comprising the top 10% probability mass are considered.
 *             We generally recommend altering this or [[temperature]] but not both.
 */
case class EditSettings(model: String = Models.textDavinciEdit001,
                        n: Option[Long] = None,
                        temperature: Option[Double] = None,
                        topP: Option[Double] = None)
