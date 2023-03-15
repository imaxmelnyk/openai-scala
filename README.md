# OpenAI Scala Client
The OpenAI Scala Client is a library that provides a Scala interface to the [OpenAI API](https://platform.openai.com/docs),
allowing you to programmatically interact with OpenAI's artificial intelligence models.
The library uses [cats](https://typelevel.org/cats/) for generic monads
and [sttp](https://sttp.softwaremill.com/en/latest/) for http client abstraction.

## Installation
Currently supported Scala versions are **2.12**, **2.13** and **3**.  
To install the OpenAI Scala Client, add the following dependency to your project's `build.sbt` file:
```scala
"dev.maxmelnyk" %% "openai-scala" % "0.2.0"
```

## Getting Started
Before using the OpenAI Scala Client, you will need to obtain an OpenAI API key
and (optionally) an organization ID as described in the [OpenAI API documentation](https://platform.openai.com/account/api-keys).
Once you have obtained your API key and organization ID, you can specify them using environment variables.
Set the `OPENAI_API_KEY` and (optionally) `OPENAI_ORG_ID` environment variables to your API key and organization ID respectively.  
Alternatively, you can explicitly specify your API key and organization ID when creating a new OpenAI client object.
See the example below:
```scala
import dev.maxmelnyk.openaiscala.client.OpenAIClient

// with organization id
val client = OpenAIClient("<your-api-key>", Some("<your-org-id>"))(sttpBackend)

// without organization id
val client = OpenAIClient("<your-api-key>")(sttpBackend)

// api key and org id taken from env vars
val client = OpenAIClient(sttpBackend)
```
In the following sections, we assume that the `OPENAI_API_KEY` and (optionally) `OPENAI_ORG_ID` environment variables have been set up correctly.

## Usage
Using the OpenAI Scala Client is straightforward if you're already using [sttp](https://sttp.softwaremill.com/en/latest/) in your project
and have a `sttpBackend` set up. Here's an example:
```scala
import dev.maxmelnyk.openaiscala.client.OpenAIClient

val sttpBackend = ???

val client = OpenAIClient(sttpBackend)
```
Please note that the [cats](https://typelevel.org/cats/)'s `MonadError[F, Throwable]` for the monad `F` used in `sttpBackend` must be implicitly in scope.

### Example with [cats effect](https://typelevel.org/cats-effect/docs/getting-started) `IO` monad and [http4s](https://github.com/http4s/http4s) as [sttp](https://sttp.softwaremill.com/en/latest/) backend
First, make sure you have the following dependencies:
```scala
// cats effect
"org.typelevel" %% "cats-effect" % "<cats_effect_version>"

// http4s client
"org.http4s" %% "http4s-dsl" % "<http4s_version>"
"org.http4s" %% "http4s-blaze-client" % "<http4s_version>"

// sttp adapter for http4s
"com.softwaremill.sttp.client3" %% "http4s-backend" % "<sttp_version>"
```

Then, in your code:
```scala
import cats.effect.{IO, IOApp}
import dev.maxmelnyk.openaiscala.client.OpenAIClient
import dev.maxmelnyk.openaiscala.models.models.Models
import dev.maxmelnyk.openaiscala.models.text.completions.chat.ChatCompletion
import sttp.client3.http4s.Http4sBackend

import java.io.File

object Main extends IOApp.Simple {
  val run: IO[Unit] = {
    val sttpBackendResource = Http4sBackend.usingDefaultBlazeClientBuilder[IO]()

    sttpBackendResource.use { sttpBackend =>
      val client = OpenAIClient(sttpBackend)

      for {
        // models api
        models <- client.listModels
        davinciModel <- client.retrieveModel(Models.davinci)

        // text apis
        completion <- client.createCompletion(Seq("This is a test."))
        chatCompletion <- client.createChatCompletion(Seq(ChatCompletion.Message(ChatCompletion.Message.Role.User, "Hello!")))
        edit <- client.createEdit("What day of the wek is it?", "Fix the spelling mistakes")

        // images api
        sourceImage = new File("/path/to/file/image.png")
        mask = new File("/path/to/file/mask.png")

        image <- client.createImage("A cute baby sea otter")
        editedImage <- client.createImageEdit(sourceImage, Some(mask), "A cute baby sea otter wearing a beret")
        variedImage <- client.createImageVariation(sourceImage)
      } yield {
        println(models)
        println(davinciModel)
        println(completion)
        println(chatCompletion)
        println(edit)
        println(image)
        println(editedImage)
        println(variedImage)
      }
    }
  }
}
```
Please note that this is just an example, and you can use any compatible monad and sttp backend with the OpenAI Scala Client.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
