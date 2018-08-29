package task4

import akka.actor.{Actor, Props}

import scala.util.Random
import task4.Protocol.{Acknowledgement, Trigger}

import scala.language.postfixOps

/**
	* Agent for receiving messages from scheduler and sending back acknowledgements.
	* @param id unique id of agent
	*/
class Agent(id:Int)  extends Actor{

	private val repeatsCount = 9
	override def receive: Receive = updated(repeatsCount)

	private def updated(remainingTriggerCount: Int):Receive = {
		case Trigger(time)=>
			println(s"$id:$time")
			val scheduler = sender()
			if(remainingTriggerCount != 0){
				val x = false
				if(x){
					throw new RuntimeException
				}
				scheduler ! Trigger(time + Random.nextInt(100))
				val triggerCount = remainingTriggerCount - 1
				context.become(updated(triggerCount))
				scheduler ! Acknowledgement
			}else{
				scheduler ! Acknowledgement
				context.stop(self)
			}
	}
}

object Agent{
	def props(id: Int): Props = Props(new Agent(id))
}
