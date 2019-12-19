package com.example.helloworldstream.impl

import akka.NotUsed
import akka.event.Logging
import akka.stream.Attributes
import akka.stream.scaladsl.Source
import com.example.helloworldstream.api.{HalfClosedWebsocketSupport, HelloWorldStreamService}
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class HelloWorldStreamServiceImpl extends HelloWorldStreamService {

  def echo = ServiceCall { source =>
    val loggingSource: Source[Int, NotUsed] = source
      .log("echoService").withAttributes(Attributes.logLevels(onElement = Logging.InfoLevel, onFinish = Logging.InfoLevel))
    Future.successful(loggingSource)
  }

  def echoWorkaround = ServiceCall { source =>
    val loggingSource: Source[Int, NotUsed] = source
      .via(HalfClosedWebsocketSupport.flow)
      .log("echoService").withAttributes(Attributes.logLevels(onElement = Logging.InfoLevel, onFinish = Logging.InfoLevel))
    Future.successful(loggingSource)
  }
}
