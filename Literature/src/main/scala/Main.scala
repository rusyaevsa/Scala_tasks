import cats.effect.IO
import cats.effect.unsafe.implicits.global

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object Main extends App{
  val nameFile: String = "shakespeare-hamlet-25.txt"
  val handler: TextHandler = new TextHandler
  val words: IO[Array[String]] = IO{handler.parseFile(nameFile)}
  val cpuPool: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

  def cpuEval[A](ioa: IO[A]): IO[A] =
    ioa.evalOn(cpuPool)

  def IOAsync(): Unit = {
    val average = for {
      wordsArr <- words
      avgLen <- cpuEval(handler.calcAvgLen(wordsArr))
    } yield
      avgLen
    val len = average.unsafeRunSync()
    println(len)
  }

  IOAsync()
}
