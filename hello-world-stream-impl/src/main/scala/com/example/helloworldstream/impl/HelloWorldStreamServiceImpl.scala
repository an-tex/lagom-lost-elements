package com.example.helloworldstream.impl

import akka.NotUsed
import akka.event.Logging
import akka.stream.Attributes
import akka.stream.scaladsl.Source
import com.example.helloworldstream.api.HelloWorldStreamService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

/**
  * Implementation of the HelloWorldStreamService.
  */
class HelloWorldStreamServiceImpl extends HelloWorldStreamService {

  def echo = ServiceCall { source =>
    val loggingSource: Source[Int, NotUsed] = source
      .log("echoService").withAttributes(Attributes.logLevels(onElement = Logging.InfoLevel))
    Future.successful(loggingSource)
  }
}
