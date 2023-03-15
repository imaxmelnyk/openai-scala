package dev.maxmelnyk.openaiscala.client

import cats.MonadError
import dev.maxmelnyk.openaiscala.config.Configuration
import dev.maxmelnyk.openaiscala.models.images._
import dev.maxmelnyk.openaiscala.models.models._
import dev.maxmelnyk.openaiscala.models.text.completions._
import dev.maxmelnyk.openaiscala.models.text.completions.chat._
import dev.maxmelnyk.openaiscala.models.text.edits._
import sttp.client3.SttpBackend

import java.io.File

/**
 * OpenAI API client.
 *
 * @tparam F generic monad type to wrap results into (ie IO, Future etc).
 */
trait OpenAIClient[F[_]] {
  /**
   * Lists the currently available models.
   *
   * [[https://platform.openai.com/docs/api-reference/models/list]]
   *
   * @return a sequence of models.
   */
  def listModels: F[Seq[Model]]

  /**
   * Retrieves a model instance.
   *
   * [[https://platform.openai.com/docs/api-reference/models/retrieve]]
   *
   * @param modelId The ID of the model to use for this request.
   * @return model instance.
   */
  def retrieveModel(modelId: String): F[Option[Model]]

  /**
   * Creates a completion for the provided prompt and settings.
   *
   * @param prompts The prompts to generate completions for.
   * @param settings The settings to use for generating completions.
   * @return completion instance.
   */
  def createCompletion(prompts: Seq[String],
                       settings: CompletionSettings = CompletionSettings()): F[Completion]

  /**
   * Creates a completion for the chat messages.
   *
   * @param messages The messages to generate chat completions for.
   * @param settings The settings to use for generating chat completions.
   * @return chat completion instance.
   */
  def createChatCompletion(messages: Seq[ChatCompletion.Message],
                           settings: ChatCompletionSettings = ChatCompletionSettings()): F[ChatCompletion]

  /**
   * Creates a new edit for the provided input, instruction, and settings.
   *
   * @param input The input text to use as a starting point for the edit.
   * @param instruction The instruction that tells the model how to edit the prompt.
   * @param settings The settings to use for generating edit.
   * @return edit instance.
   */
  def createEdit(input: String,
                 instruction: String,
                 settings: EditSettings = EditSettings()): F[Edit]

  /**
   * Creates an image given a prompt.
   *
   * @param prompt A text description of the desired image(s). The maximum length is 1000 characters.
   * @param settings The settings to use for creating images.
   * @return image instance.
   */
  def createImage(prompt: String,
                  settings: ImageSettings = ImageSettings()): F[Image]

  /**
   * Creates an edited or extended image given an original image and a prompt.
   *
   * @param image The image to edit. Must be a valid PNG file, less than 4MB, and square.
   *              If mask is not provided, image must have transparency, which will be used as the mask.
   * @param mask An additional image whose fully transparent areas (e.g. where alpha is zero) indicate where image should be edited.
   *             Must be a valid PNG file, less than 4MB, and have the same dimensions as image.
   * @param prompt A text description of the desired image(s). The maximum length is 1000 characters.
   * @param settings The settings to use for editing images.
   * @return image instance.
   */
  def createImageEdit(image: File,
                      mask: Option[File],
                      prompt: String,
                      settings: ImageSettings = ImageSettings()): F[Image]

  /**
   *
   * @param image The image to use as the basis for the variation(s).
   *              Must be a valid PNG file, less than 4MB, and square.
   * @param settings The settings to use for creating image variations.
   * @return image instance.
   */
  def createImageVariation(image: File,
                           settings: ImageSettings = ImageSettings()): F[Image]
}

object OpenAIClient {
  /**
   * Creates a default instance of [[OpenAIClient]].
   *
   * @param apiKey OpenAI API key.
   * @param orgIdOpt OpenAI organization ID.
   * @param sttpBackend sttp backend to use for requests.
   * @param monadError monad error instance, so we know how to operate with provided monad type.
   * @tparam F generic monad type to wrap results into (ie IO, Future etc).
   * @return an instance of [[OpenAIClient]].
   */
  def apply[F[_]](apiKey: String,
                  orgIdOpt: Option[String] = None)
                 (sttpBackend: SttpBackend[F, Any])
                 (implicit monadError: MonadError[F, Throwable]): OpenAIClient[F] = {
    new DefaultOpenAIClient(apiKey, orgIdOpt)(sttpBackend)
  }

  /**
   * Creates a default instance of [[OpenAIClient]] using config values for api key and org id.
   *
   * @param sttpBackend sttp backend to use for requests.
   * @param monadError monad error instance, so we know how to operate with provided monad type.
   * @tparam F generic monad type to wrap results into (ie IO, Future etc).
   * @return an instance of [[OpenAIClient]].
   */
  def apply[F[_]](sttpBackend: SttpBackend[F, Any])
                 (implicit monadError: MonadError[F, Throwable]): OpenAIClient[F] = {
    OpenAIClient(Configuration.openAiApiKey, Configuration.openAiOrgId)(sttpBackend)
  }
}
