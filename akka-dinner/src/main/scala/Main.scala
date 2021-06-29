import akka.actor.{ActorRef, ActorSystem}

import scala.language.postfixOps

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("PhilosopherDinner")
  val waiter: ActorRef = system.actorOf(Waiter.props())
  val Socrat: ActorRef = system.actorOf(Philosopher.props("Сократ",0,1))
  val Platon: ActorRef = system.actorOf(Philosopher.props("Платон",1,2))
  val Diogen: ActorRef = system.actorOf(Philosopher.props("Диоген",2,3))
  val Kant: ActorRef = system.actorOf(Philosopher.props("Кант",3,4))
  val Aristotel: ActorRef = system.actorOf(Philosopher.props("Аристотель",4,0))
  Socrat ! Philosopher.Thinking(waiter)
  Platon ! Philosopher.Thinking(waiter)
  Diogen ! Philosopher.Thinking(waiter)
  Kant ! Philosopher.Thinking(waiter)
  Aristotel ! Philosopher.Thinking(waiter)
}
