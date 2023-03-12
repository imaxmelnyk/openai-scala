package dev.maxmelnyk.openaiscala.client

import cats.MonadError
import cats.syntax.all._
import com.typesafe.scalalogging.LazyLogging
import dev.maxmelnyk.openaiscala.exceptions.OpenAIClientException
import dev.maxmelnyk.openaiscala.models.{ChatCompletion, Completion, Model}
import dev.maxmelnyk.openaiscala.models.settings.{CreateChatCompletionSettings, CreateCompletionSettings}
import dev.maxmelnyk.openaiscala.utils.BodySerializers._
import dev.maxmelnyk.openaiscala.utils.JsonImplicits._
import io.circe.parser.{decode, parse}
import sttp.client3.{SttpBackend, UriContext, basicRequest}
import sttp.model.{HeaderNames, MediaType, StatusCode}

private[client] class DefaultOpenAIClient[F[_]](private val apiKey: String,
                                                private val orgIdOpt: Option[String])
                                               (private val sttpBackend: SttpBackend[F, Any])
                                               (private implicit val monadError: MonadError[F, Throwable])
  extends OpenAIClient[F]
    with LazyLogging {

  import DefaultOpenAIClient._

  // request headers, usually (always for now) it's enough
  private val defaultHeaders: Map[String, String] = {
    val commonHeaders = Map(
      HeaderNames.ContentType -> MediaType.ApplicationJson.toString,
      HeaderNames.Authorization -> s"Bearer $apiKey")

    // add org id header if it's present in config
    orgIdOpt match {
      case Some(orgId) => commonHeaders + (orgIdHeaderName -> orgId)
      case None => commonHeaders
    }
  }

  def listModels: F[Seq[Model]] = {
    logger.debug("Retrieving models")

    basicRequest
      .get(uri"$baseUrl/models")
      .headers(defaultHeaders)
      .send(sttpBackend)
      .map { response =>
        (response.code, response.body) match {
          // success case
          case (StatusCode.Ok, Right(responseBody)) =>
            val responseBodyJson = parse(responseBody).getOrElse {
              throw OpenAIClientException(s"Failed to parse response body: $responseBody")
            }

            // the actual models are in "data" field
            responseBodyJson.hcursor.downField("data").as[List[Model]] match {
              case Left(error) =>
                throw OpenAIClientException(s"Failed to decode response body: $responseBody", error)
              case Right(models) =>
                logger.debug(s"Retrieved ${models.size} models")
                models
            }

          // unexpected case
          case (statusCode, responseBody) =>
            throw OpenAIClientException(
              "Failed to retrieve models: " +
                s"status: $statusCode, " +
                s"body: ${responseBody.fold(identity, identity)}")
        }
      }
  }

  def retrieveModel(modelId: String): F[Option[Model]] = {
    logger.debug(s"Retrieving $modelId model")

    basicRequest
      .get(uri"$baseUrl/models/$modelId")
      .headers(defaultHeaders)
      .send(sttpBackend)
      .map { response =>
        (response.code, response.body) match {
          // success case
          case (StatusCode.Ok, Right(responseBody)) =>
            decode[Model](responseBody) match {
              case Left(error) =>
                throw OpenAIClientException(s"Failed to decode response body: $responseBody", error)
              case Right(model) =>
                logger.debug(s"Retrieved $modelId model")
                Some(model)
            }

          // not found case
          case (StatusCode.NotFound, _) =>
            logger.debug(s"Model $modelId not found")
            None

          // unexpected case
          case (statusCode, responseBody) =>
            throw OpenAIClientException(
              s"Failed to retrieve $modelId model: " +
                s"status: $statusCode, " +
                s"body: ${responseBody.fold(identity, identity)}")
        }
      }
  }

  def createCompletion(prompts: Seq[String],
                       settings: CreateCompletionSettings = CreateCompletionSettings()): F[Completion] = {
    logger.debug(s"Creating completion for ${prompts.length} prompts")

    basicRequest
      .post(uri"$baseUrl/completions")
      .body((prompts, settings))
      .headers(defaultHeaders)
      .send(sttpBackend)
      .map { response =>
        (response.code, response.body) match {
          // success case
          case (StatusCode.Ok, Right(responseBody)) =>
            decode[Completion](responseBody) match {
              case Left(error) =>
                throw OpenAIClientException(s"Failed to decode response body: $responseBody", error)
              case Right(completion) =>
                logger.debug(s"Created completion with ${completion.choices.length} choices")
                completion
            }

          // unexpected case
          case (statusCode, responseBody) =>
            throw OpenAIClientException(
              "Failed to create completion: " +
                s"status: $statusCode, " +
                s"body: ${responseBody.fold(identity, identity)}")
        }
      }
  }

  def createChatCompletion(messages: Seq[ChatCompletion.Message],
                           settings: CreateChatCompletionSettings = CreateChatCompletionSettings()): F[ChatCompletion] = {
    logger.debug(s"Creating chat completion for ${messages.length} messages")

    basicRequest
      .post(uri"$baseUrl/chat/completions")
      .body((messages, settings))
      .headers(defaultHeaders)
      .send(sttpBackend)
      .map { response =>
        (response.code, response.body) match {
          // success case
          case (StatusCode.Ok, Right(responseBody)) =>
            decode[ChatCompletion](responseBody) match {
              case Left(error) =>
                println(error)
                throw OpenAIClientException(s"Failed to decode response body: $responseBody", error)
              case Right(chatCompletion) =>
                logger.debug(s"Created chat completion with ${chatCompletion.choices.length} choices")
                chatCompletion
            }

          // unexpected case
          case (statusCode, responseBody) =>
            throw OpenAIClientException(
              "Failed to create chat completion: " +
                s"status: $statusCode, " +
                s"body: ${responseBody.fold(identity, identity)}")
        }
      }
  }
}

private[client] object DefaultOpenAIClient {
  private val baseUrl: String = "https://api.openai.com/v1"
  private val orgIdHeaderName = "OpenAI-Organization"
}
