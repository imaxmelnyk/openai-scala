package dev.maxmelnyk.openaiscala.models.text.completions

import java.time.LocalDateTime

/**
 * [[https://platform.openai.com/docs/api-reference/completions]]
 */
case class Completion(id: String,
                      created: LocalDateTime,
                      model: String,
                      choices: Seq[Completion.Choice],
                      usage: Completion.Usage)

object Completion {
  case class Choice(text: String,
                    index: Long,
                    logprobs: Option[Choice.Logprobs],
                    finishReason: String)

  object Choice {
    case class Logprobs(tokens: Seq[String],
                        tokenLogprobs: Seq[Double],
                        topLogprobs: Seq[Map[String, Double]],
                        textOffset: Seq[Long])
  }

  case class Usage(promptTokens: Long,
                   completionTokens: Long,
                   totalTokens: Long)
}
