import scala.collection.mutable.ListBuffer
import scala.io.Source

object  Main extends App {
  val score = new Score("src\\sheet.txt")

  println(score.noteList)

}