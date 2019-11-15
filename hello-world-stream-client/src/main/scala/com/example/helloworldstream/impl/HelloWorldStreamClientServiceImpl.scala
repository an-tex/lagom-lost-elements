package com.example.helloworldstream.impl

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.example.helloworldstream.api.{HelloWorldStreamClientService, HelloWorldStreamService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class HelloWorldStreamClientServiceImpl(
                                         helloWorldStreamService: HelloWorldStreamService,
                                         implicit val executionContext: ExecutionContext,
                                         implicit val actorSystem: ActorSystem
                                       ) extends HelloWorldStreamClientService {

  implicit val actorMaterializer = ActorMaterializer()

  private final val log: Logger = LoggerFactory.getLogger(this.getClass)

  def test = ServiceCall { _ =>
    val ticker = Source.tick(1.second, 100.millis, true)
    helloWorldStreamService.stream.invoke().flatMap(_.zip(ticker).map {
      case (msg, _) =>
        log.info(s"received $msg")
        msg
    }.runWith(Sink.seq))
  }
}
