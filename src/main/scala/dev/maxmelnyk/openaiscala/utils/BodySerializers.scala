package dev.maxmelnyk.openaiscala.utils

import dev.maxmelnyk.openaiscala.exceptions.OpenAIClientException
import dev.maxmelnyk.openaiscala.models.images.requests._
import dev.maxmelnyk.openaiscala.models.text.completions.chat.requests._
import dev.maxmelnyk.openaiscala.models.text.completions.requests._
import dev.maxmelnyk.openaiscala.models.text.edits.requests._
import dev.maxmelnyk.openaiscala.utils.JsonImplicits._
import io.circe.Json
import io.circe.syntax._
import sttp.client3.{BasicRequestBody, BodySerializer, RequestBody, StringBody, multipart, multipartFile}
import sttp.model.{MediaType, Part}

import java.nio.charset.StandardCharsets

private[openaiscala] object BodySerializers {

  // Text stuff

  implicit val createCompletionBodySerializer: BodySerializer[CreateCompletionRequest] = {
    case CreateCompletionRequest(prompts, settings) =>
      val settingsJson = settings.asJson.dropNullValues
      val additionalJson = Map("prompt" -> prompts).asJson
      val fullJson = settingsJson.deepMerge(additionalJson)

      StringBody(fullJson.noSpaces, StandardCharsets.UTF_8.toString, MediaType.ApplicationJson)
  }

  implicit val createChatCompletionBodySerializer: BodySerializer[CreateChatCompletionRequest] = {
    case CreateChatCompletionRequest(messages, settings) =>
      val settingsJson = settings.asJson.dropNullValues
      val additionalJson = Map("messages" -> messages).asJson
      val fullJson = settingsJson.deepMerge(additionalJson)

      StringBody(fullJson.noSpaces, StandardCharsets.UTF_8.toString, MediaType.ApplicationJson)
  }

  implicit val createEditBodySerializer: BodySerializer[CreateEditRequest] = {
    case CreateEditRequest(input, instruction, settings) =>
      val settingsJson = settings.asJson.dropNullValues
      val additionalJson = Map("input" -> input, "instruction" -> instruction).asJson
      val fullJson = settingsJson.deepMerge(additionalJson)

      StringBody(fullJson.noSpaces, StandardCharsets.UTF_8.toString, MediaType.ApplicationJson)
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
