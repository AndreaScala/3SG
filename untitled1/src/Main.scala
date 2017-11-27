object  Main extends App {
  val score = new Score("src\\sheet.txt")
  println("Inserire BPM: ")
  val myBpm = scala.io.StdIn.readInt()
  //Interpretazione e stampa Note
  for (note: Note <- score.noteList){
    println(note.getNoteValue(myBpm) + ": " + note.getNoteName() + note.getOctave())
  }

  println("L'ottava centrale è la "+score.getCentralOctave())


}