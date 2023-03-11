package dev.maxmelnyk.openaiscala.client

import cats.instances.all.catsStdInstancesForTry
import dev.maxmelnyk.openaiscala.exceptions.OpenAIClientException
import dev.maxmelnyk.openaiscala.models.Completion.Choice
import dev.maxmelnyk.openaiscala.models.{Completion, ModelInfo, Models}
import dev.maxmelnyk.openaiscala.models.settings.CreateCompletionSettings
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{SttpBackend, Response => SttpResponse}
import sttp.model.{Method, StatusCode}
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
}
