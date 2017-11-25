import scala.collection.mutable.ListBuffer
import scala.io.Source

object  Main extends App {
  //Si preferisce utilizzare un ListBuffer anzichè una lista in quanto inserire elementi in fondo ad una lista ha complessità O(n)
  val noteList =  ListBuffer[Note]()
  //Lettura file di testo derivato dalla lettura della partitura
  val bufferedSource = Source.fromFile("src\\sheet.txt")
  //Ciclo su ogni riga del file di testo (NB.: il file di testo viene generato appositamente con una nota per riga)
  for (line <- bufferedSource.getLines) {
    //Divisione fra Pitch e Duration
    val noteArray = line.split("t").toList
    //Isolazione Pitch
    val nowPitch = noteArray.head.toInt
    //Isolazione Duration (l'ultimo char viene eliminato in quanto è sempre ';')
    val nowDuration = noteArray.last.dropRight(1).toFloat
    //Incapsulamento delle info estratte in un oggetto Nota
    val nowNote = new Note(nowPitch,nowDuration)
    //Aggiunta della nota al ListBuffer
    noteList.append(nowNote)
  }
  //Stampa di prova
  println(noteList)
  //Chiusura del file di testo
  bufferedSource.close
  //Da qui in poi il buffer è pronto ad essere iterato
}