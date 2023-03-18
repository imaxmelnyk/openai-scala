package dev.maxmelnyk.openaiscala.models.text.edits

import java.time.LocalDateTime

/**
 * [[https://platform.openai.com/docs/api-reference/edits]]
 */
case class Edit(created: LocalDateTime,
                choices: Seq[Edit.Choice],
                usage: Edit.Usage)

object Edit {
  case class Choice(text: String,
                    index: Long)

  case class Usage(promptTokens: Long,
                   completionTokens: Long,
                   totalTokens: Long)
}
