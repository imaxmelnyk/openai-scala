package dev.maxmelnyk.openaiscala.models.text.completions.chat.requests

import dev.maxmelnyk.openaiscala.models.text.completions.chat.{ChatCompletion, ChatCompletionSettings}

/**
 * [[https://platform.openai.com/docs/api-reference/chat/create]]
 *
 * @param messages The messages to generate chat completions for.
 * @param settings The settings to use for completing chats.
 */
private[openaiscala] case class CreateChatCompletionRequest(messages: Seq[ChatCompletion.Message],
                                                            settings: ChatCompletionSettings)
