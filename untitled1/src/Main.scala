object  Main extends App {
  println("SCALA'S SCALE by Scala & Gerloni")
  println("Con questo programma è possibile:\n" +
    "1) Dato un file di testo generato da un MIDI identificare le note e i loro valori\n" +
    "2) Calcolare l'ottava centrale di riferimento\n" +
    "3) Calcolare (con approssimazione statistica) la tonalità principale del brano\n" +
    "4) Calcolare il tempo (la misura) del brano")
  println("\nInserire nome file e BPM: ")
  val command = scala.io.StdIn.readLine().split(' ')
  val myBpm = command(1).toInt
  val score = new Score("src\\" + command(0)+ ".txt")

  score.print(myBpm)

  println("\nL'ottava centrale è la "+score.getCentralOctave())

  println("\nLa tonalità è (presumibilmente) "+score.getTonality()+" Maggiore")

  println("\nIl tempo del brano è "+score.getTempo(myBpm))

}