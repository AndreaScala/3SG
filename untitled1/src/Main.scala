import scala.io.Source

object  Main extends App {
  val bufferedSource = Source.fromFile("src\\sheet.txt")
  for (line <- bufferedSource.getLines) {
    val noteArray = line.split("t").toList
    val nowPitch = noteArray.head.toInt
    val nowDuration = noteArray.last.dropRight(1).toFloat
    val nowNote = new Note(nowPitch,nowDuration)
    println(nowNote)
  }

  bufferedSource.close
}