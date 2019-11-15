package com.example.helloworldstream.impl

import com.example.helloworldstream.api.{HelloWorldStreamClientService, HelloWorldStreamService}
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class HelloWorldStreamClientLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new HelloWorldStreamClientApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new HelloWorldStreamClientApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[HelloWorldStreamClientService])
}

abstract class HelloWorldStreamClientApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[HelloWorldStreamClientService](wire[HelloWorldStreamClientServiceImpl])

  // Bind the HelloWorldService client
  lazy val helloWorldStreamService: HelloWorldStreamService = serviceClient.implement[HelloWorldStreamService]
}
