package task1

import akka.actor.ActorSystem
import task1.MessageForwarderActor.Message

object Main extends App {

  var n = 10
  if (args.length != 0) {
    n = args(0).toInt
  }
  val system: ActorSystem = ActorSystem("test-task")
  var lastActor = system.actorOf(MessageForwarderActor.props(n, n, null))
  for (i <- 1 to n) {
    lastActor = system.actorOf(MessageForwarderActor.props(n - i, n, lastActor),
                               s"actor$i")
  }

  system.actorSelection("/user/actor1") ! Message(0)
}
