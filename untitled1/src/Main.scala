object  Main extends App {
  println("SCALA'S SCALE by Scala & Gerloni")
  println("Con questo programma è possibile:\n" +
    " - Dato un file di testo generato da un MIDI identificare le note e i loro valori\n" +
    " - Calcolare l'ottava centrale di riferimento\n" +
    " - Calcolare (con approssimazione statistica) la tonalità principale del brano\n" +
    " - Calcolare il tempo (la misura) del brano")
  println("\nInserire nome file e BPM: ")
  val command = scala.io.StdIn.readLine().split(' ')
  val myBpm = command(1).toInt
  val score = new Score("src\\" + command(0)+ ".txt")
  while (true) {
    println("\nCosa si desidera fare?\n" +
      "1) Analizza note partitura\n" +
      "2) Rileva Ottava Centrale\n" +
      "3) Rileva Tonalità\n" +
      "4) Rileva Tempo\n" +
      "0) Esci dall'apllicazione")

    var options = scala.io.StdIn.readLine().split(' ')

    for(option <- options) {
      option.toInt match {
        case 1 => score.print (myBpm)
        case 2 => println ("\nL'ottava centrale è la " + score.getCentralOctave () )
        case 3 => println ("\nLa tonalità è (presumibilmente) " + score.getTonality () + " Maggiore")
        case 4 => println ("\nIl tempo del brano è " + score.getTempo (myBpm) )
        case 0 => {
          println("\nGrazie per aver usato SCALA's SCALE by Scala & Gerloni\nCatania, 1 Dicembre 2017")
          System.exit(0)
        }
        case _ => println("Comando non valido")
      }
    }
  }
}