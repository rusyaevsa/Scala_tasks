import State.{Free, State}
import akka.actor.{Actor, ActorRef, Props}

import scala.collection.mutable.ArrayBuffer

object Waiter{
  case class PhilosopherWithForks(philosopher: ActorRef, forkL: Int, forkR: Int)
  sealed trait Message
  case class RequestForks(forkL: Int, forkR: Int) extends Message
  case class FreeForks(forkL: Int, forkR: Int) extends Message
  def props(): Props = Props(new Waiter)
}

object State extends Enumeration{
  type State = Value
  val Busy, Reserved, Free = Value
}

class Waiter extends Actor{
  import Waiter._
  val forks: ArrayBuffer[State] = ArrayBuffer(State.Free, State.Free, State.Free, State.Free, State.Free)
  val queue: ArrayBuffer[PhilosopherWithForks] = ArrayBuffer()
  override def receive: Receive = {
    case RequestForks(forkL, forkR) =>
      if (forks(forkL) == State.Free && forks(forkR) == State.Free) {
        forks(forkL) = State.Busy
        forks(forkR) = State.Busy
        sender() ! Philosopher.Eating
      }
      else {
        queue.addOne(PhilosopherWithForks(sender(), forkL, forkR))
        if (forks(forkL) == Free)
          forks(forkL) = State.Reserved
        else if (forks(forkR) == Free)
          forks(forkR) = State.Reserved
        sender() ! Philosopher.Waiting(true)
      }
      println(queue)
      println(forks)
    case FreeForks(forkL, forkR) =>
      if (queue.exists(_.forkR == forkL))
        forks(forkL) = State.Reserved
      else
        forks(forkL) = State.Free
      if (queue.exists(_.forkL == forkR))
        forks(forkR) = State.Reserved
      else
        forks(forkR) = State.Free
      val list: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
      for (i <- queue.indices){
        val indexL: Int = queue(i).forkL
        val indexR: Int = queue(i).forkR
        if (forks(indexL) != State.Busy && forks(indexR) != State.Busy) {
          forks(indexL) = State.Busy
          forks(indexR) = State.Busy
          list.addOne(i)
          queue(i).philosopher ! Philosopher.Eating
        }
      }
      println("--------------------------------")
      println(queue)
      for (elem <- list.reverse) {
        queue.remove(elem)
        println(elem)
      }
      println(queue)
      println("*********************************")
      sender() ! Philosopher.Thinking(self)
  }
}
