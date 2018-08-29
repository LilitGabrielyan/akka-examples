##Tests Examples of AKKA

* Case 1

Application with n‐actors. Each actor should be assigned an id between 1 to n (unique). 
The message object should contain a field numberOfHopsTravelled (int, initialized to 0).
Actor 1 forwards the message to the actor with id 2 when receiving the message from actor
1, but before doing so, increases the numberOfHopsTravelled by one in the message. This
continues in this way until the message reaches actor n. When actor n receives the message,
it prints out the numberOfHopsTravelled field in the received message.

* Case 2

Create an n‐actor system (unique ids from 1 to n), where each actor i can only send and
receive messages to/from actor i‐1 and i+1 (actor n can communicate with actor n‐1 an
actor 0).
When an actor has received a message from both of its neighbors, it prints out its own Id and
the message it received.

* Case 3

Creates a system with n agents and one additional actor which we refer to as a “scheduler”.
Messages are passed
between the agents and the scheduler, containing a time (between 0 and +inf). There are
two types of messages: 1.) Trigger 2.) Acknowledgement. Each agent receiving a Trigger
message from the scheduler and sends a new Trigger message to the scheduler (with time of
received trigger + random number between 0 and100. It also sends an Acknowledgement
message to the scheduler after that. The agent repeats such behavior for max 10 times
(meaning during the whole program it receives 10 Trigger messages from scheduler, sends 9
Trigger messages back to the Scheduler and sends 10 Acknowledgement messages to back
10 scheduler; there is one less Trigger message sent back, in order to eventually stop the
“simulation”).
The scheduler upon receiving a Trigger messages stores the message and actor reference it in
a sorted Trigger queue (sorted by time). Whenever the Scheduler receives an
Acknowledgement message and there are still more Triggers in the sorted queue, it sends
out the first Trigger message to the corresponding agent actor.
Each agent should write down in the console log, its id and Trigger message time whenever it
receives a new Trigger message from the scheduler.
The scheduler is initialized with one Trigger message per agent (with random time between 0
and 100). After initialization a Start message is sent to the scheduler, which initiates the
“simulation”: The scheduler removes the first message in the sorted Trigger queue and sends
the first Trigger to the corresponding agent.



> Notes:
> In first case actor to which message is sent is passed to actor as ref this is more preferable as it can be tested.
> In second case actors are find by selector which is another way to communicate with other actors.