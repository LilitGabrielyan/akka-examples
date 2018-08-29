package task3

import akka.actor.{Actor, Props}
import task3.NeighborhoodActor.Message

/**
	* Actor for sending incoming message to its previous and next actors (previous and next are defined by id number)
	* until it has received message from both neighbors.
	* @param id unique id of actor
	* @param totalCount total number of actors in system
	*/
class NeighborhoodActor(id: Int, totalCount: Int) extends Actor {

	override def receive: Receive =
		waitingForReplies(
			Set(prevActorId, nextActorId)
		)

	def waitingForReplies(remainingToReceive: Set[Int]): Receive = {
		case Message(msg) =>
			val actorRef = sender()
			val actorName = actorRef.path.elements.last
			val next = nextActorId
			val prev = prevActorId
			if (actorName.equals(s"actor$next") || actorName.equals(s"actor$prev")) {
				val senderID = obtainIdFromName(actorName)
				val newRemainingToReceive = remainingToReceive - senderID
				if (newRemainingToReceive.isEmpty) {
					println(s"ID:$senderID Message: $msg")
				} else {
					context.become(waitingForReplies(newRemainingToReceive))
					newRemainingToReceive.foreach { id => context.actorSelection(s"../actor$id") ! Message(msg) }
				}
			} else {
				remainingToReceive.foreach { id => context.actorSelection(s"../actor$id") ! Message(msg) }
			}
	}

	private def nextActorId = if (id == totalCount) 1 else id + 1
	private def prevActorId = if (id == 1) totalCount else id - 1
	private def obtainIdFromName(actorName:String) = if (actorName.equals(s"actor$nextActorId")) nextActorId else prevActorId

}

object NeighborhoodActor {

	def props(id: Int, totalCount: Int): Props = Props(new NeighborhoodActor(id, totalCount))

	final case class Message(msg: String)

}
