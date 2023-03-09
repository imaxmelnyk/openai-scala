package dev.maxmelnyk.openaiscala.models

import java.time.LocalDateTime

/**
 * [[https://platform.openai.com/docs/api-reference/models]]
 */
case class Model(id: String,
                 created: LocalDateTime,
                 ownedBy: String,
                 permission: Seq[Model.Permission],
                 root: String,
                 parent: Option[String])

object Model {
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
