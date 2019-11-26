package com.example.helloworldstream.impl

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.example.helloworldstream.api.{HelloWorldStreamClientService, HelloWorldStreamService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class HelloWorldStreamClientServiceImpl(
                                         helloWorldStreamService: HelloWorldStreamService,
                                         implicit val executionContext: ExecutionContext,
                                         implicit val actorSystem: ActorSystem
                                       ) extends HelloWorldStreamClientService {

  implicit val actorMaterializer = ActorMaterializer()

  private final val log: Logger = LoggerFactory.getLogger(this.getClass)

  type Tick = Boolean

  def test = ServiceCall { _ =>
    val ticker = Source.tick(3.second, 1.second, true)
    val loggingSink = Sink.fold[(Long, Long), (Array[Byte], Tick)]((0L, 0L)) {
      case ((previousMessages, previousBytes), (currentBytes, _)) =>
        val nextMessages = previousMessages + 1
        val nextBytes = previousBytes + currentBytes.length
        log.info(s"RECEIVED $nextMessages messages ($nextBytes bytes)")
        (nextMessages, nextBytes)
    }
    helloWorldStreamService.stream.invoke().flatMap(
      _.zip(ticker).runWith(loggingSink)
    )
    Future.successful(NotUsed)
  }
}
