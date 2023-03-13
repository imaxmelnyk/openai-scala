package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.exceptions.OpenAIClientException
import dev.maxmelnyk.openaiscala.models.ChatCompletion
import dev.maxmelnyk.openaiscala.models.images.requests._
import dev.maxmelnyk.openaiscala.models.settings._
import dev.maxmelnyk.openaiscala.utils.JsonImplicits._
import io.circe.Json
import io.circe.syntax._
import sttp.client3.{BasicRequestBody, BodySerializer, RequestBody, StringBody, multipart, multipartFile}
import sttp.model.{MediaType, Part}

import java.nio.charset.StandardCharsets
import scala.language.implicitConversions


private[openaiscala] object BodySerializers {
  implicit val createCompletionBodySerializer: BodySerializer[(Seq[String], CreateCompletionSettings)] = {
    case (prompts: Seq[String], settings: CreateCompletionSettings) =>
      val bodyJsonString: String = settings
        .asJson
        .deepMerge(Map("prompt" -> prompts).asJson)
        .dropNullValues
        .noSpaces

      StringBody(bodyJsonString, StandardCharsets.UTF_8.toString, MediaType.ApplicationJson)
  }

  implicit val createChatCompletionBodySerializer: BodySerializer[(Seq[ChatCompletion.Message], CreateChatCompletionSettings)] = {
    case (messages: Seq[ChatCompletion.Message], settings: CreateChatCompletionSettings) =>
      val bodyJsonString: String = settings
        .asJson
        .deepMerge(Map("messages" -> messages).asJson)
        .dropNullValues
        .noSpaces

      StringBody(bodyJsonString, StandardCharsets.UTF_8.toString, MediaType.ApplicationJson)
  }

  implicit val createEditBodySerializer: BodySerializer[(String, String, CreateEditSettings)] = {
    case (input: String, instruction: String, settings: CreateEditSettings) =>
      val bodyJsonString: String = settings
        .asJson
        .deepMerge(Map("input" -> input, "instruction" -> instruction).asJson)
        .dropNullValues
        .noSpaces

      StringBody(bodyJsonString, StandardCharsets.UTF_8.toString, MediaType.ApplicationJson)
  }

  // Images stuff

  implicit val createImageRequestBodySerializer: BodySerializer[CreateImageRequest] = {
    case CreateImageRequest(prompt, settings) =>
      val settingsJson = settings.asJson.dropNullValues
      val additionalJson = Map("prompt" -> prompt).asJson
      val fullJson = settingsJson.deepMerge(additionalJson)

      StringBody(fullJson.noSpaces, StandardCharsets.UTF_8.toString, MediaType.ApplicationJson)
  }

  implicit def imageEditRequestToMultiParts(request: CreateImageEditRequest): Seq[Part[RequestBody[Any]]] = {
    val settingsJson = request.settings.asJson.dropNullValues

    val partsWithoutMask =
      jsonToParts(settingsJson) :+
        multipart("prompt", request.prompt) :+
        multipartFile("image", request.image)

    request.mask match {
      case Some(mask) => partsWithoutMask :+ multipartFile("mask", mask)
      case _ => partsWithoutMask
    }
  }

  implicit def imageVariationRequestToMultiParts(request: CreateImageVariationRequest): Seq[Part[RequestBody[Any]]] = {
    val settingsJson = request.settings.asJson.dropNullValues

    jsonToParts(settingsJson) :+
      multipartFile("image", request.image)
  }


  // for cases when request body is multipart instead of json
  private def jsonToParts(json: Json): Seq[Part[BasicRequestBody]] = {
    json
      .asObject
      .getOrElse(throw OpenAIClientException("Failed to encode json to object"))
      .toMap
      .map { case (name, jsonValue) =>
        val stringValue = jsonValue
          .asString
          .getOrElse(throw OpenAIClientException("Failed to encode json field to string"))

        multipart(name, stringValue)
      }
      .toList
  }
}
