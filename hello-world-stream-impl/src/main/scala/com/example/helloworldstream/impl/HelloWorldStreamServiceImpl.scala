package com.example.helloworldstream.impl

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.example.helloworldstream.api.HelloWorldStreamService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

/**
  * Implementation of the HelloWorldStreamService.
  */
class HelloWorldStreamServiceImpl extends HelloWorldStreamService {

  private final val log: Logger = LoggerFactory.getLogger(this.getClass)

  def stream: ServiceCall[NotUsed, Source[Int, NotUsed]] = ServiceCall { _ =>
    Future.successful(
      Source(1.to(1000)).wireTap(msg => log.info(s"sending $msg"))
    )
  }
}
