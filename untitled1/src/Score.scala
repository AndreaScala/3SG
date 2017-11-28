import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
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

  //stampa dell'oggetto score
  def print(myBpm: Int): Unit = {
    println("###INIZIO###")
    for (note <- noteList){
      println(note.getNoteValue(myBpm) + ": " + note.getNoteName() + note.getOctave())
    }
    println("###FINE###")
  }

  //funzione che calcola l'ottava centrale come media delle ottave
  def getCentralOctave(): Int = {
    var average = 0f
    var deviation = 0f
    for (note <- noteList) {
      average+=note.getOctave()
    }
    average = average/noteList.length.toFloat
    //aggiunto arrotondamento
    deviation = average - average.toInt.toFloat
    if (deviation >= 0.5)
      return (average.toInt + 1)
    else
      return average.toInt
  }

  //funzione che utilizza il rilevamento delle alterazioni per riconoscere la tonalità
  //per ogni alterazione rilevata si setta a true il valore corrispondente nella Map con chiavi le alterazioni
  //quindi si controlla quale alterazione è stata rilevata dando "priorità secondo il circolo delle 5"
  //es: è stato rilevato "FA#", allora solo lui è in chiave, di conseguenza la tonalità è sol maggiore
  //es: è stato rilvevato "SOL#", allora sono in chiave anche FA# e DO# e non serve controllare, la tonalità è LA M
  //es: nessuna alterazione rilevata: la tonalità è DO maggiore
  def getTonality(): String = {
    val sharpsFound = Map ("FA#"-> false, "DO#" -> false,"SOL#"-> false,"RE#"-> false, "LA#"-> false)
    for (note <- noteList) {
     if (sharpsFound.contains(note.getNoteName())) sharpsFound(note.getNoteName())=true
    }
    val note = new Note(0,0)
    var sharpToCheck = "LA#"
    for (i <- 1 to sharpsFound.size) {
      if (sharpsFound(sharpToCheck)==true) {
        return note.getNoteName(note.getRelativePitchByNoteName(sharpToCheck)+1)
      }
      else
        sharpToCheck = note.getNoteName((note.getRelativePitchByNoteName(sharpToCheck)+5)%12)
    }
    return "DO"
  }
}
