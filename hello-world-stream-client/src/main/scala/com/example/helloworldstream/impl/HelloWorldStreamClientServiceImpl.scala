package com.example.helloworldstream.impl

import akka.NotUsed
import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, Attributes}
import com.example.helloworldstream.api.{HelloWorldStreamClientService, HelloWorldStreamService}
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.{ExecutionContext, Future}

class HelloWorldStreamClientServiceImpl(
                                         helloWorldStreamService: HelloWorldStreamService,
                                         implicit val executionContext: ExecutionContext,
                                         implicit val actorSystem: ActorSystem
                                       ) extends HelloWorldStreamClientService {

  implicit val actorMaterializer = ActorMaterializer()

  def test = ServiceCall { _ =>
    val source: Source[Int, NotUsed] = Source(1.to(8))
    val loggingSource: Source[Int, NotUsed] = source.log("clientSending")
      .withAttributes(Attributes.logLevels(onElement = Logging.InfoLevel, onFinish = Logging.InfoLevel))
    val loggingSink: Sink[Int, Future[Seq[Int]]] = Flow[Int]
      .log("clientReceived").withAttributes(Attributes.logLevels(onElement = Logging.InfoLevel, onFinish = Logging.InfoLevel))
      .toMat(Sink.seq[Int])(Keep.right)

    // this works:
    //loggingSource.runWith(loggingSink)

    // this doesn't as it finishes too early and looses elements, even though the echo service is doing exactly the same by returning the source it was invoked with
    helloWorldStreamService.echo.invoke(loggingSource).flatMap(_.runWith(loggingSink))
  }
}
