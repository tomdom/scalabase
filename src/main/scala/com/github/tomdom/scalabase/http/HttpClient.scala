package com.github.tomdom.scalabase.http

import akka.actor.ActorSystem
import akka.io.IO

import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.util.Try

import spray.can.Http
import spray.client.pipelining._
import spray.http._
import spray.httpx.unmarshalling._

object HttpClient {
  def get[O](url: String, handling: Try[O] => Unit)(implicit unmarshaller: FromResponseUnmarshaller[O]): Unit =
    (new HttpClient(handling)).request(Get(url))

  def post[O](url: String, content: HttpEntity, handling: Try[O] => Unit)(implicit unmarshaller: FromResponseUnmarshaller[O]): Unit = {
    (new HttpClient(handling)).request(Post(url, content))
  }
}

class HttpClient[O](handling: Try[O] => Unit)(implicit unmarshaller: FromResponseUnmarshaller[O]) {
  lazy val config = ConfigFactory.load("httpclient")
  implicit lazy val system = ActorSystem("httpclient", config)
  import system.dispatcher

  def request(r: HttpRequest): Unit = {
    val pipeline: HttpRequest => Future[O] = sendReceive ~> unmarshal[O]

    pipeline(r) onComplete (handling andThen shutdown)
  }

  def shutdown(v: Unit): Unit = {
    IO(Http) ! Http.CloseAll
    system.shutdown()
  }
}
