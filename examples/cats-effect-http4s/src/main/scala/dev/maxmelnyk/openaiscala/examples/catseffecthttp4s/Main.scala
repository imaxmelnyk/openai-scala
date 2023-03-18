package dev.maxmelnyk.openaiscala.examples.catseffecthttp4s

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
