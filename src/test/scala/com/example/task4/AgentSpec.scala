package com.example.task4

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import task4.Agent
import task4.Protocol.{Acknowledgement, Trigger}


class AgentSpec() extends TestKit(ActorSystem("TestSpec")) with ImplicitSender
		with WordSpecLike  with Matchers with BeforeAndAfterAll {

	override def afterAll: Unit = {
		TestKit.shutdownActorSystem(system)
	}

	"reply to trigger request" in {
		val probe = TestProbe()
		val agent = probe.childActorOf(Agent.props(2))

		probe.send(agent, Trigger(10))
		probe.expectMsgType[Trigger]
		probe.expectMsg(Acknowledgement)
	}


}
