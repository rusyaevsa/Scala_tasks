import Waiter.{FreeForks, RequestForks}
import akka.actor.{Actor, ActorRef, Props}

import scala.util.Random

object Philosopher{
  sealed trait Action
  case class Thinking(waiter: ActorRef) extends Action
  case class Waiting(state: Boolean) extends Action
  case object Eating extends Action

  def props(name: String, forkL: Int, forkR: Int): Props = Props(new Philosopher(name, forkL, forkR))
}

class Philosopher(private val name: String, private val forkL: Int, private val forkR: Int) extends Actor{
  import Philosopher._
  var waitState: Boolean = false
  val random: Random = Random
  override def receive: Receive = {
    case Thinking(waiter) =>
      val time: Int = random.nextInt(100)
      for (_ <- 0 to time) {
        Thread.sleep(1000)
        println(s"$name: думаю")
      }
      waiter ! RequestForks(forkL, forkR)
    case Waiting(true) =>
      println(s"$name: думаю и жду")
    case Eating =>
      waitState = false
      val time: Int = 10
      for (_ <- 0 to time) {
        Thread.sleep(1000)
        println(s"$name: кушаю")
      }
      sender() ! FreeForks(forkL, forkR)
  }
}
