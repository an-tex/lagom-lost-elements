package com.example.helloworldstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait HelloWorldStreamService extends Service {

  def echo : ServiceCall[Source[Int, NotUsed], Source[Int, NotUsed]]

  override final def descriptor: Descriptor = {
    import Service._

    named("hello-world-stream")
      .withCalls(
        namedCall("echo", echo),
      ).withAutoAcl(true)
  }
}

