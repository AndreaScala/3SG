object  Main extends App {
  println("Inserire nome file e BPM: ")
  val command = scala.io.StdIn.readLine().split(' ')
  val myBpm = command(1).toInt
  val score = new Score("src\\" + command(0)+ ".txt")

  score.print(myBpm)

  println("L'ottava centrale è la "+score.getCentralOctave())

  println("La tonalità è (presumibilmente) "+score.getTonality()+" Maggiore")

}