package dev.maxmelnyk.openaiscala.models.text.completions.chat

import enumeratum.EnumEntry.Snakecase
import enumeratum.{Enum, EnumEntry}

import java.time.LocalDateTime

/**
 * [[https://platform.openai.com/docs/api-reference/chat]]
 */
case class ChatCompletion(id: String,
                          created: LocalDateTime,
                          model: String,
                          choices: Seq[ChatCompletion.Choice],
                          usage: ChatCompletion.Usage)

object ChatCompletion {
  case class Message(role: Message.Role,
                     content: String)

  object Message {
    sealed trait Role extends EnumEntry with Snakecase
    object Role extends Enum[Role] {
      case object System extends Role
      case object User extends Role
      case object Assistant extends Role

      val values = findValues
    }
  }

  case class Choice(message: Message,
                    index: Long,
                    finishReason: String)

  case class Usage(promptTokens: Long,
                   completionTokens: Long,
                   totalTokens: Long)
}
