package dev.maxmelnyk.openaiscala.client

import cats.MonadError
import cats.syntax.all._
import com.typesafe.scalalogging.LazyLogging
import dev.maxmelnyk.openaiscala.config.Config
import dev.maxmelnyk.openaiscala.exceptions.OpenAIClientException
import dev.maxmelnyk.openaiscala.models.Model
import sttp.client3.{SttpBackend, UriContext, basicRequest}
import sttp.model.{HeaderNames, MediaType, StatusCode}
import io.circe.parser.{decode, parse}
import dev.maxmelnyk.openaiscala.utils.JsonImplicits._

trait OpenAIClient[F[_]] {
  def listModels(): F[Seq[Model]]

  def retrieveModel(modelId: String): F[Option[Model]]
}

private class DefaultOpenAIClient[F[_]](sttpBackend: SttpBackend[F, Any])
                                       (implicit monadError: MonadError[F, Throwable])
  extends OpenAIClient[F]
    with LazyLogging {

  import DefaultOpenAIClient._

  def listModels(): F[Seq[Model]] = catchUnknownErrors {
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

            responseBodyJson.hcursor.downField("data").as[Seq[Model]] match {
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

  def retrieveModel(modelId: String): F[Option[Model]] = catchUnknownErrors {
    logger.debug(s"Retrieving $modelId model")

    basicRequest
      .get(uri"$baseUrl/models")
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

  // it just wraps all the unknown errors to the known wrapper
  private def catchUnknownErrors[A](f: => F[A]): F[A] = {
    f.adaptError{
      case e: OpenAIClientException => e
      case e: Throwable => OpenAIClientException("Unknown error", e)
    }
  }
}

private object DefaultOpenAIClient {
  private val baseUrl: String = "https://api.openai.com/v1"
  private val orgIdHeaderName = "OpenAI-Organization"

  // usually (always for now) it's enough
  private val defaultHeaders: Map[String, String] = {
    val commonHeaders = Map(
      HeaderNames.ContentType -> MediaType.ApplicationJson.toString,
      HeaderNames.Authorization -> s"Bearer ${Config.openAiApiKey}")

    // add org id header if it's present in config
    Config.openAiOrgId match {
      case Some(orgId) => commonHeaders + (orgIdHeaderName -> orgId)
      case None => commonHeaders
    }
  }
}
