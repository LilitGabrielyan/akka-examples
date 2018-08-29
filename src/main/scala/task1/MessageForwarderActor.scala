package task1

import akka.actor.{Actor, ActorRef, Props}
import task1.MessageForwarderActor.Message

/**
  * Actor for forwarding incoming messages to next actors until mentioned maximum number of hops.
  *
  * @param id         unique id of actor
  * @param totalCount maximum count of hops message can be forwarded
  * @param nextActor  actor to whom message will be forwarded
  */
class MessageForwarderActor(id: Int, totalCount: Int, nextActor: ActorRef)
  extends Actor {

  override def receive: PartialFunction[Any, Unit] = {

    case Message(numberOfHopsTravelled, times) =>
      val receivedTime = System.currentTimeMillis()
      val newTimes = times :+ receivedTime
      if (numberOfHopsTravelled == totalCount - 1) {
        for ((time, index) <- newTimes.view.zipWithIndex)
          println(s"Actor ${index + 1}, message received $time")
      } else {
        nextActor ! Message(numberOfHopsTravelled + 1, newTimes)
      }

  }
}

object MessageForwarderActor {
  def props(id: Int, totalCount: Int, nextActor: ActorRef): Props =
    Props(new MessageForwarderActor(id, totalCount, nextActor))

  final case class Message(numberOfHopsTravelled: Int,
                           times: List[Long] = List.empty) {
    override def equals(obj: scala.Any): Boolean = obj match {
      case obj: Message =>
        obj.numberOfHopsTravelled == this.numberOfHopsTravelled
      case _ => false
    }
  }

}
