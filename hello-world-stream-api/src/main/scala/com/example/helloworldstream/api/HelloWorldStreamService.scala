package com.example.helloworldstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * The Hello World stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the HelloWorldStream service.
  */
trait HelloWorldStreamService extends Service {

  def stream: ServiceCall[NotUsed, Source[Array[Byte], NotUsed]]

  override final def descriptor: Descriptor = {
    import Service._

    named("hello-world-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

