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

  println("Cosa si desidera fare?\n" +
    "1) Analizza note partitura\n" +
    "2) Rileva Ottava Centrale\n" +
    "3) Rileva Tonalità\n" +
    "4) Rileva Tempo")

  val options = scala.io.StdIn.readLine().split(' ')

  for(option <- options) {
    option.toInt match {
      case 1 => score.print (myBpm)
      case 2 => println ("\nL'ottava centrale è la " + score.getCentralOctave () )
      case 3 => println ("\nLa tonalità è (presumibilmente) " + score.getTonality () + " Maggiore")
      case 4 => println ("\nIl tempo del brano è " + score.getTempo (myBpm) )
      case _ => println("Comando non valido")
    }
  }
}