package dev.maxmelnyk.openaiscala.client

import cats.instances.all.catsStdInstancesForTry
import dev.maxmelnyk.openaiscala.exceptions.OpenAIClientException
import dev.maxmelnyk.openaiscala.models.{ChatCompletion, Completion, ModelInfo, Models}
import dev.maxmelnyk.openaiscala.models.settings.{CreateChatCompletionSettings, CreateCompletionSettings}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{SttpBackend, Response => SttpResponse}
import sttp.model.StatusCode
import sttp.monad.TryMonad

import java.time.LocalDateTime
import scala.util.{Failure, Success, Try}

class DefaultOpenAIClientSpec extends AnyFlatSpec with Matchers {
  private val sttpBackendStub: SttpBackendStub[Try, Any] = SttpBackendStub(TryMonad)
  private val client: SttpBackend[Try, Any] => DefaultOpenAIClient[Try] = new DefaultOpenAIClient[Try]("openai_api_key", None)(_)

  behavior of "DefaultOpenAIClient"

  behavior of "listModels"

  it should "succeed" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("models")))
      .thenRespond {
        val responseBody =
          """{
               "object": "list",
               "data": [
                 {
                   "id": "gpt-3.5-turbo",
                   "object": "model",
                   "created": 1677610602,
                   "owned_by": "openai",
                   "permission": [
                     {
                       "id": "modelperm-QvbW9EnkbwPtWZuEYnCBAMsO",
                       "object": "model_permission",
                       "created": 1678193854,
                       "allow_create_engine": false,
                       "allow_sampling": true,
                       "allow_logprobs": true,
                       "allow_search_indices": false,
                       "allow_view": true,
                       "allow_fine_tuning": false,
                       "organization": "*",
                       "group": null,
                       "is_blocking": false
                     }
                   ],
                   "root": "gpt-3.5-turbo",
                   "parent": null
                 },
                 {
                   "id": "davinci",
                   "object": "model",
                   "created": 1649359874,
                   "owned_by": "openai",
                   "permission": [
                     {
                       "id": "modelperm-U6ZwlyAd0LyMk4rcMdz33Yc3",
                       "object": "model_permission",
                       "created": 1669066355,
                       "allow_create_engine": false,
                       "allow_sampling": true,
                       "allow_logprobs": true,
                       "allow_search_indices": false,
                       "allow_view": true,
                       "allow_fine_tuning": false,
                       "organization": "*",
                       "group": null,
                       "is_blocking": false
                     }
                   ],
                   "root": "davinci",
                   "parent": null
                 }
               ]
             }"""

        SttpResponse(responseBody, StatusCode.Ok)
      }

    client(sttpBackend).listModels match {
      case Success(models) =>
        models should have length 2
        models should contain theSameElementsAs List(
          ModelInfo(
            "gpt-3.5-turbo",
            LocalDateTime.of(2023, 2, 28, 18, 56, 42),
            "openai",
            List(
              ModelInfo.Permission(
                "modelperm-QvbW9EnkbwPtWZuEYnCBAMsO",
                LocalDateTime.of(2023, 3, 7, 12, 57, 34),
                allowCreateEngine = false,
                allowSampling = true,
                allowLogprobs = true,
                allowSearchIndices = false,
                allowView = true,
                allowFineTuning = false,
                "*",
                None,
                isBlocking = false)),
            "gpt-3.5-turbo",
            None),
          ModelInfo(
            "davinci",
            LocalDateTime.of(2022, 4, 7, 19, 31, 14),
            "openai",
            List(
              ModelInfo.Permission(
                "modelperm-U6ZwlyAd0LyMk4rcMdz33Yc3",
                LocalDateTime.of(2022, 11, 21, 21, 32, 35),
                allowCreateEngine = false,
                allowSampling = true,
                allowLogprobs = true,
                allowSearchIndices = false,
                allowView = true,
                allowFineTuning = false,
                "*",
                None,
                isBlocking = false)),
            "davinci",
            None))
      case _ => fail()
    }
  }

  it should "fail on unexpected response" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("models")))
      .thenRespond {
        SttpResponse("{}", StatusCode.InternalServerError)
      }

    client(sttpBackend).listModels match {
      case Failure(_: OpenAIClientException) => succeed
      case _ => fail()
    }
  }

  it should "fail on unexpected response body (wrong json)" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("models")))
      .thenRespond {
        SttpResponse("{ } 123}", StatusCode.Ok)
      }

    client(sttpBackend).listModels match {
      case Failure(_: OpenAIClientException) => succeed
      case _ => fail()
    }
  }

  it should "fail on unexpected response body (missing fields)" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("models")))
      .thenRespond {
        val responseBody =
          """{
               "no-data": []
             }"""

        SttpResponse(responseBody, StatusCode.Ok)
      }

    client(sttpBackend).listModels match {
      case Failure(_: OpenAIClientException) => succeed
      case _ => fail()
    }
  }

  behavior of "retrieveModel"

  it should "succeed when model exists" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("models", "gpt-3.5-turbo")))
      .thenRespond {
        val responseBody =
          """{
               "id": "gpt-3.5-turbo",
               "object": "model",
               "created": 1677610602,
               "owned_by": "openai",
               "permission": [
                 {
                   "id": "modelperm-QvbW9EnkbwPtWZuEYnCBAMsO",
                   "object": "model_permission",
                   "created": 1678193854,
                   "allow_create_engine": false,
                   "allow_sampling": true,
                   "allow_logprobs": true,
                   "allow_search_indices": false,
                   "allow_view": true,
                   "allow_fine_tuning": false,
                   "organization": "*",
                   "group": null,
                   "is_blocking": false
                 }
               ],
               "root": "gpt-3.5-turbo",
               "parent": null
             }"""

        SttpResponse(responseBody, StatusCode.Ok)
      }

    client(sttpBackend).retrieveModel("gpt-3.5-turbo") match {
      case Success(Some(model)) =>
        model shouldEqual ModelInfo(
          "gpt-3.5-turbo",
          LocalDateTime.of(2023, 2, 28, 18, 56, 42),
          "openai",
          List(
            ModelInfo.Permission(
              "modelperm-QvbW9EnkbwPtWZuEYnCBAMsO",
              LocalDateTime.of(2023, 3, 7, 12, 57, 34),
              allowCreateEngine = false,
              allowSampling = true,
              allowLogprobs = true,
              allowSearchIndices = false,
              allowView = true,
              allowFineTuning = false,
              "*",
              None,
              isBlocking = false)),
          "gpt-3.5-turbo",
          None)
      case _ => fail()
    }
  }

  it should "succeed when model doesn't exist" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("models", "gpt-4")))
      .thenRespond {
        val responseBody =
          """{
               "error": {
                 "message": "That model does not exist",
                 "type": "invalid_request_error",
                 "param": "model",
                 "code": null
               }
             }"""

        SttpResponse(responseBody, StatusCode.NotFound)
      }

    client(sttpBackend).retrieveModel("gpt-4") match {
      case Success(None) => succeed
      case _ => fail()
    }
  }

  it should "fail on unexpected response" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("models", "gpt-3.5-turbo")))
      .thenRespond {
        SttpResponse("{}", StatusCode.InternalServerError)
      }

    client(sttpBackend).retrieveModel("gpt-3.5-turbo") match {
      case Failure(_: OpenAIClientException) => succeed
      case _ => fail()
    }
  }

  it should "fail on unexpected response body (missing fields)" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("models", "gpt-3.5-turbo")))
      .thenRespond {
        val responseBody =
          """{
               "id": "gpt-3.5-turbo"
             }"""

        SttpResponse(responseBody, StatusCode.Ok)
      }

    client(sttpBackend).retrieveModel("gpt-3.5-turbo") match {
      case Failure(_: OpenAIClientException) => succeed
      case _ => fail()
    }
  }

  behavior of "createCompletion"

  it should "succeed" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("completions")))
      .thenRespond {
        val responseBody =
          """{
               "id": "cmpl-uqkvlQyYK7bGYrRHQ0eXlWi7",
               "object": "text_completion",
               "created": 1589478378,
               "model": "text-davinci-003",
               "choices": [
                 {
                   "text": "\n\nThis is indeed a test",
                   "index": 0,
                   "logprobs": null,
                   "finish_reason": "length"
                 }
               ],
               "usage": {
                 "prompt_tokens": 5,
                 "completion_tokens": 7,
                 "total_tokens": 12
               }
             }"""

        SttpResponse(responseBody, StatusCode.Ok)
      }

    val prompts = List("Say this is a test")
    val settings = CreateCompletionSettings(
      model = Models.textDavinci003,
      maxTokens = Some(7),
      temperature = Some(0),
      topP = Some(1),
      n = Some(1),
      stop = Some(List("\n")))

    client(sttpBackend).createCompletion(prompts, settings) match {
      case Success(completion) =>
        completion shouldEqual Completion(
          "cmpl-uqkvlQyYK7bGYrRHQ0eXlWi7",
          LocalDateTime.of(2020, 5, 14, 17, 46, 18),
          Models.textDavinci003,
          List(Completion.Choice("\n\nThis is indeed a test", 0, None, "length")),
          Completion.Usage(5, 7, 12))
      case _ => fail()
    }
  }

  it should "fail on unexpected response" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("completions")))
      .thenRespond {
        SttpResponse("{}", StatusCode.InternalServerError)
      }

    client(sttpBackend).createCompletion(List("This gonna fail...")) match {
      case Failure(_: OpenAIClientException) => succeed
      case _ => fail()
    }
  }

  behavior of "createChatCompletion"

  it should "succeed" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("chat", "completions")))
      .thenRespond {
        val responseBody =
          """{
               "id": "chatcmpl-123",
               "object": "chat.completion",
               "created": 1677652288,
               "model": "gpt-3.5-turbo-0301",
               "choices": [{
                 "index": 0,
                 "message": {
                   "role": "assistant",
                   "content": "\n\nHello there, how may I assist you today?"
                 },
                 "finish_reason": "stop"
               }],
               "usage": {
                 "prompt_tokens": 9,
                 "completion_tokens": 12,
                 "total_tokens": 21
               }
             }"""

        SttpResponse(responseBody, StatusCode.Ok)
      }

    val messages = List(ChatCompletion.Message(ChatCompletion.Message.Role.User, "Hello!"))
    val settings = CreateChatCompletionSettings(model = Models.gpt35Turbo)

    client(sttpBackend).createChatCompletion(messages, settings) match {
      case Success(chatCompletion) =>
        chatCompletion shouldEqual ChatCompletion(
          "chatcmpl-123",
          LocalDateTime.of(2023, 3, 1, 6, 31, 28),
          Models.gpt35Turbo0301,
          List(ChatCompletion.Choice(ChatCompletion.Message(ChatCompletion.Message.Role.Assistant, "\n\nHello there, how may I assist you today?"), 0, "stop")),
          ChatCompletion.Usage(9, 12, 21))
      case _ => fail()
    }
  }

  it should "fail on unexpected response" in {
    val sttpBackend = sttpBackendStub
      .whenRequestMatches(_.uri.path.endsWith(List("chat", "completions")))
      .thenRespond {
        SttpResponse("{}", StatusCode.InternalServerError)
      }

    client(sttpBackend).createChatCompletion(List(ChatCompletion.Message(ChatCompletion.Message.Role.User, "This gonna fail..."))) match {
      case Failure(_: OpenAIClientException) => succeed
      case _ => fail()
    }
  }
}
