package com.example

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import task1.MessageForwarderActor
import task1.MessageForwarderActor.Message
import akka.actor._
import akka.testkit.TestProbe

class MessageForwaarderTest()
    extends TestKit(ActorSystem("TestSpec"))
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "reply to registration requests" in {
    val probe = TestProbe()
    val deviceActor = system.actorOf(MessageForwarderActor.props(1, 1, probe.ref))

		deviceActor ! Message(10, List.empty)
    probe.expectMsg(Message(11, List(System.currentTimeMillis())))
  }

  "An Echo actor" must {

    "send back messages unchanged" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "hello world"
      expectMsg("hello world")
    }

  }

}
