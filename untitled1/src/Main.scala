object  Main extends App {
  val score = new Score("src\\sheet.txt")
  println("Inserire BPM: ")
  val myBpm = scala.io.StdIn.readInt()
  //Interpretazione e stampa Note
  for (note: Note <- score.noteList){
    println(note.findNoteType(myBpm) + ": " + note.getNoteName() + note.getOctave())
  }

}