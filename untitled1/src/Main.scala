object  Main extends App {
  val score = new Score("src\\sheet.txt")
  println("Inserire BPM: ")

  val myBpm = scala.io.StdIn.readInt()
  score.print(myBpm)

  println("L'ottava centrale è la "+score.getCentralOctave())

  println("La tonalità è (presumibilmente) "+score.getTonality()+" Maggiore")

}