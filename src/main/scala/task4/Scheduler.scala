package task4

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorNotFound, ActorRef, OneForOneStrategy, Props}
import task4.Protocol.{Acknowledgement, Start, Trigger}

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.language.postfixOps
import scala.util.Random

/**
	* Scheduler for organizing message distribution to agents.
	* Scheduler organizes messages in following way:
	* In initial queue it keeps random agents after receiving start message pops firs agent and sends message to it
	* then receives acknowledgement and again pops from queue until queue is empty.
	* @param numberOfAgents total number of agents in system
	*/
class Scheduler(numberOfAgents: Int) extends Actor {


	val ord: Ordering[(Int, ActorRef)] = (i1: (Int, ActorRef), i2: (Int, ActorRef)) => i1._1.compareTo(i2._1)

	private val queue: mutable.PriorityQueue[(Int, ActorRef)] = new mutable.PriorityQueue()(ord)

	1.to(numberOfAgents).foreach(i => queue.+=((Random.nextInt(100), context.actorOf(Agent.props(i), s"agent$i"))))


	override val supervisorStrategy: OneForOneStrategy =
		OneForOneStrategy() {
			case _: RuntimeException =>
				Restart
		}


	override def receive: Receive = updated(queue)


	private def updated(queue: mutable.PriorityQueue[(Int, ActorRef)]):Receive = {
		case Trigger(time) =>
			val agent = sender()
			queue.+=((time, agent))
			context.become(updated(queue))
		case Acknowledgement =>
			if (queue.nonEmpty) {
				val (time: Int, agent: ActorRef) = queue.dequeue()
				context.become(updated(queue))
				agent ! Trigger(time)
			}else{
				implicit val timeout: FiniteDuration = 0.1 seconds

				Await.result(context.system.actorSelection(s"/user/agent/*").resolveOne(timeout).map(Some(_)).recover{
					case _:ActorNotFound => None
				}, timeout)  match {
					case None =>
						context.system.stop(self)
						context.system.terminate()
				}

			}
		case Start =>
			val (time: Int, agent: ActorRef) = queue.dequeue()
			context.become(updated(queue))
			agent ! Trigger(time)
	}

}

object Scheduler {
	def props(id: Int): Props = Props(new Scheduler(id))
}