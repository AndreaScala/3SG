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

  //funzione pseudoeuristica che utilizza il rilevamento delle alterazioni per riconoscere la tonalità
  //per ogni alterazione rilevata si incrementa di 3 il valore corrispondente nella Map con chiavi le alterazioni,e si
  //decrementano di 1 le restanti. In questo modo è probabile che le note alterate meno frequenti si annullano,
  //infine si controlla quale alterazione è stata rilevata dando "priorità secondo il circolo delle 5"
  //es: è stato rilevato "FA#", allora solo lui è in chiave, di conseguenza la tonalità è sol maggiore
  //es: è stato rilvevato "SOL#", allora sono in chiave anche FA# e DO# e non serve controllare, la tonalità è LA M
  //es: nessuna alterazione rilevata: la tonalità è DO maggiore
  def getTonality(): String = {
    val sharpsFound = Map ("FA#"-> 0, "DO#" -> 0,"SOL#"-> 0,"RE#"-> 0, "LA#"-> 0)
    for (note <- noteList if sharpsFound.contains(note.getNoteName())) {
      sharpsFound(note.getNoteName())+=3
      for (key <- sharpsFound.keys if key!=note.getNoteName()) sharpsFound(key)-= 1
    }
    val note = new Note(0,0)
    var sharpToCheck = "LA#"
    for (i <- 1 to sharpsFound.size) {
      if (sharpsFound(sharpToCheck)>0) {
        return note.getNoteName(note.getRelativePitchByNoteName(sharpToCheck)+1)
      }
      else
        sharpToCheck = note.getNoteName((note.getRelativePitchByNoteName(sharpToCheck)+5)%12)
    }
    return "DO"
  }

  //Per il calcolo del tempo del brano si sceglie una sequenza di tempi (5/4, 4/4, 3/4, 2/4, 3/8) e si testa il brano intero:
  //Per ogni tempo scelto si raggruppano i valori delle note del brano, controllando che le somme dei valori consecutivi
  //rientrino all'interno del tempo scelto e non sforino. Ogni volta che il test non funziona, si testa il tempo successivo.
  //Implementazione: un contatore a saturazione si resetta ogni volta che la somma dei valori dei brani raggiunge il tempo
  //Se è riuscito a testare tutto il brano e terminare col valore zero allora il tempo del brano è stato individuato.
  def getTempo(bpm: Int): String = {
    var barSaturator = 0f
    var value = 1.5f;
    while (value <100) {
      barSaturator = 0f
      for (note <- noteList if barSaturator < value) {
        barSaturator+=note.getNumericNoteValue(note.getNoteValue(bpm))
        barSaturator = if (barSaturator==value) 0 else barSaturator
      }
      if (barSaturator==0)
        return value match {
          case eighth if eighth%1!=0 => (2f*eighth).toInt.toString +"/8"
          case quarter => quarter.toInt.toString + "/4"
        }
      value+=0.5f
    }
    return "...boh, sarà progressive rock"
  }
}
