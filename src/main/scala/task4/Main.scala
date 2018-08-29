package task4

import akka.actor.ActorSystem
import task4.Protocol.Start


object Main extends App {

	val system: ActorSystem = ActorSystem("test-task")
	private val n = 10
	val scheduler = system.actorOf(Scheduler.props(n), "scheduler")
	scheduler ! Start
}
