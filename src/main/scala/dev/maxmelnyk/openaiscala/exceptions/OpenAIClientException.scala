package dev.maxmelnyk.openaiscala.exceptions

final case class OpenAIClientException(message: String, cause: Throwable) extends Exception(message, cause)

object OpenAIClientException {
  def apply(message: String): OpenAIClientException = OpenAIClientException(message, null)
}
