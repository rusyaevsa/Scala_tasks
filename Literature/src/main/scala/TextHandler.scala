import cats.effect.IO

import scala.io.Source


class TextHandler {
  def parseFile(nameFile: String): Array[String] = {
    val file = Source.fromFile(nameFile)
    val text: String = file.mkString.toLowerCase
    file.close()
    text.split("[^a-z']").filter(_.nonEmpty)
  }

  def calcAvgLen(words: Array[String]): IO[Double] = {
    IO{words.map(_.length).sum * 1.0 / words.length}
  }
}