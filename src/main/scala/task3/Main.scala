package task3

import akka.actor.ActorSystem
import task3.NeighborhoodActor.Message

import scala.util.Random

object Main extends App {

	val system: ActorSystem = ActorSystem("test-task")
	private val n = 10
	for(i <- 1 to n) {
		system.actorOf(NeighborhoodActor.props(i, n), s"actor$i")
	}
	val actorId = Random.nextInt(n)+1
	system.actorSelection(s"/user/actor$actorId") ! Message(s"Hello from: $actorId")
}
