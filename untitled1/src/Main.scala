import scala.io.Source

object  Main extends App {
  val bufferedSource = Source.fromFile("src\\sheet.txt")
  for (line <- bufferedSource.getLines) {
    println(line)
  }

  bufferedSource.close
}