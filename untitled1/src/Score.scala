import scala.collection.mutable.ListBuffer
import scala.io.Source

class Score (var sheetPath: String){

  //CONSTRUCTOR

  //Si preferisce utilizzare un ListBuffer anzichè una lista in quanto inserire elementi in fondo ad una lista ha complessità O(n)
  val noteList =  ListBuffer[Note]()
  //Lettura file di testo derivato dalla lettura della partitura
  val bufferedSource = Source.fromFile(sheetPath)
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
  bufferedSource.close;

  //END CONSTRUCTOR

  //def countNote(note: Note): Int{}

  //def getCentralOctave: Note {}

  def convertPitchToNameNote(pitch: Int) = (pitch%12)match {
    case 0 => "DO"
    case 2 => "RE"
    case 4 => "MI"
    case 5 => "FA"
    case 7 => "SOL"
    case 9 => "LA"
    case 11 => "SI"
    case _ => convertPitchToNameNote(pitch-1)+"#";
  }



}
