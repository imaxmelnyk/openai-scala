package dev.maxmelnyk.openaiscala.models

import java.time.LocalDateTime

/**
 * [[https://platform.openai.com/docs/api-reference/models]]
 */
case class ModelInfo(id: String,
                     created: LocalDateTime,
                     ownedBy: String,
                     permission: Seq[ModelInfo.Permission],
                     root: String,
                     parent: Option[String])

object ModelInfo {
  case class Permission(id: String,
                        created: LocalDateTime,
                        allowCreateEngine: Boolean,
                        allowSampling: Boolean,
                        allowLogprobs: Boolean,
                        allowSearchIndices: Boolean,
                        allowView: Boolean,
                        allowFineTuning: Boolean,
                        organization: String,
                        group: Option[String],
                        isBlocking: Boolean)
}
