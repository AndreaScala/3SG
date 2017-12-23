import java.io.StreamCorruptedException

import scala.io.Source
import org.ddahl.rscala._

object Main extends App {
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
      "5) Scrivi note su file (Formato DURATA NOTA OTTAVA con prima riga BPM)\n" +
      "6) Scrivi note su file (Formato MIDI & PURE DATA / PITCHtTIME)\n" +
      "7) Riproduci melodia\n" +
      "8) Riproduci melodia con BPM a scelta\n" +
      "9) Trasporta melodia\n" +
      "0) Esci dall'applicazione")

    var options = scala.io.StdIn.readLine().split(' ')
    for(option <- options) {
      try {
        option.toInt match {
          case 1 => score.print (myBpm)
          case 2 => println ("\nL'ottava centrale è la " + score.getCentralOctave)
          case 3 => println ("\nLa tonalità è (presumibilmente) " + score.getTonality + " Maggiore")
          case 4 => {
            val tempo = score.getTempo(myBpm)
            if (tempo!=(0,0))println ("\nIl tempo del brano è " + tempo._1.toString + "/" + tempo._2.toString)
            else println("Il brano è progressive e ho fagghiato a trovare il tempo\n")
          }
          case 5 | 6 => {
            println("Con che nome salvare il file?")
            if (option.toInt==5) score.printOnFile(myBpm, "src\\"+scala.io.StdIn.readLine() + ".txt")
            else score.printOnFile("src\\"+scala.io.StdIn.readLine() + ".txt")
            println("Scrittura su file eseguita con successo")
          }
          case 7 | 8 => {
            var newBpm = 0
            if (option.toInt==8) {
              println("Scegli i BPM nuovi")
              newBpm = scala.io.StdIn.readInt()
            }
            else
              newBpm = myBpm
            val R = RClient()
            R.scoreString = score.createStringOfNotes(newBpm)
            val playerSource = Source.fromFile("src\\Playe.R")
            R.eval(playerSource.mkString)
          }
          case 9 => {
            println("Di quanti semitoni trasportare la melodia?")
            score.transpose(scala.io.StdIn.readInt())
            println("Transposizione effettuata!")
          }
          case 0 => {
            println("\nGrazie per aver usato SCALA's SCALE by Scala & Gerloni\nCatania, 6 Dicembre 2017")
            System.exit(0)
          }
          case _ => println("Comando non valido")
        }
      } catch {
        case e if option=="somebody" => {
          var scoreBody = new Score("src\\" +option + ".txt");
          println("SOMEBODY")
          var scoreCopy = new Score("src\\" + command(0)+ ".txt")
          var i :Int = 0
          for (note <- scoreCopy.noteList) {
            note.pitch = scoreBody.noteList(i).pitch
            i=(i+1)%scoreBody.noteList.length
          }
          val R = RClient()
          R.scoreString = scoreCopy.createStringOfNotes(myBpm)
          val playerSource = Source.fromFile("src\\Playe.R")
          R.eval(playerSource.mkString)
        }
        case e if option.toInt==0 => System.exit(0)
        case e => println("Comando non valido")
      }
    }
  }
}
