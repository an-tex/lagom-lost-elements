package com.example.helloworldstream.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait HelloWorldStreamClientService extends Service {

  def test: ServiceCall[NotUsed, Seq[Int]]

  def testWorkaround: ServiceCall[NotUsed, Seq[Int]]

  override final def descriptor: Descriptor = {
    import Service._
    named("hello-world-stream-client")
      .withCalls(
        pathCall("/api/test", test),
        pathCall("/api/testWorkaround", testWorkaround),
      )
      .withAutoAcl(true)
  }
}

