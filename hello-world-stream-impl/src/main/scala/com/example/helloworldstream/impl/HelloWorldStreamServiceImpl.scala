package com.example.helloworldstream.impl

import akka.NotUsed
import akka.stream.scaladsl.{Sink, Source}
import com.example.helloworldstream.api.HelloWorldStreamService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

/**
  * Implementation of the HelloWorldStreamService.
  */
class HelloWorldStreamServiceImpl extends HelloWorldStreamService {

  private final val log: Logger = LoggerFactory.getLogger(this.getClass)

  def stream: ServiceCall[NotUsed, Source[Array[Byte], NotUsed]] = ServiceCall { _ =>
    val byte = '.'.toByte
    val message = Array.fill(16000)(byte)
    val messages = List.fill(1024)(message).toIterator
    val loggingSink = Sink.fold[(Long, Long), Array[Byte]]((0L, 0L)) {
      case ((previousMessages, previousBytes), currentBytes) =>
        val nextMessages = previousMessages + 1
        val nextBytes = previousBytes + currentBytes.length
        log.info(s"SENT $nextMessages messages ($nextBytes bytes)")
        (nextMessages, nextBytes)
    }
    Future.successful(
      Source.fromIterator(() => messages).wireTap(loggingSink)
    )
  }
}
