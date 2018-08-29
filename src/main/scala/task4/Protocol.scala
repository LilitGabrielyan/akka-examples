package task4

/**
	* Type of messages that can be sent back and forward in system.
	*/
object Protocol {
	sealed trait  Message
	case object Start extends Message
	final case class Trigger(time:Int) extends Message
	case object Acknowledgement extends Message

}
