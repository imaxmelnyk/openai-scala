package dev.maxmelnyk.openaiscala.models.models

/**
 * Contains some frequently used models.
 * [[https://platform.openai.com/docs/models]]
 */
object Models {
  // GPT-3.5
  val gpt35Turbo = "gpt-3.5-turbo"
  val gpt35Turbo0301 = "gpt-3.5-turbo-0301"
  val textDavinci003 = "text-davinci-003"
  val textDavinci002 = "text-davinci-002"

  // Whisper
  val whisper1 = "whisper-1"

  // Embeddings
  val textEmbeddingAda002 = "text-embedding-ada-002"
  val textSearchAdaDoc001 = "text-search-ada-doc-001"

  // Codex
  val codeDavinci002 = "code-davinci-002"
  val codeCushman001 = "code-cushman-001"

  // Moderation
  val textModerationLatest = "text-moderation-latest"
  val textModerationStable = "text-moderation-stable"

  // GPT-3
  val textCurie001 = "text-curie-001"
  val textBabbage001 = "text-babbage-001"
  val textAda001 = "text-ada-001"
  val davinci = "davinci"
  val curie = "curie"
  val babbage = "babbage"
  val ada = "ada"

  // Edits
  val textDavinciEdit001 = "text-davinci-edit-001"
  val codeDavinciEdit001 = "code-davinci-edit-001"
}
