class Note (var pitch: Int, var time: Float) {
  //Per stampe di prova
  override def toString: String =
    s"(Altezza MIDI: $pitch, Durata: $time)"

  //Funzione di Pattern Matching per individuare il nome della nota
  def findNoteName(): String = {
    this.pitch%12 match {
      case 0 => "DO"
      case 1 => "DO#"
      case 2 => "RE"
      case 3 => "RE#"
      case 4 => "MI"
      case 5 => "FA"
      case 6 => "FA#"
      case 7 => "SOL"
      case 8 => "SOL#"
      case 9 => "LA"
      case 10 => "LA#"
      case 11 => "SI"
    }
  }

  //Funzione di Pattern Matching per individuare l'ottava della nota
  def findOctave(): Int = {
    this.pitch/12 match {
      case 0 => -1
      case 1 => 0
      case 2 => 1
      case 3 => 2
      case 4 => 3
      case 5 => 4
      case 6 => 5
      case 7 => 6
      case 8 => 7
      case 9 => 8
      case 10 => 9
    }
  }

  //Funzione di Pattern Matching per identificare il tipo della nota (Figura)
  //NB.: Va approfondita
  def findNoteType(bpm: Int): String = {
    val beatTime = 60000/bpm
    val wholeTime = beatTime*4
    val halfTime = beatTime*2
    val quarterTime = beatTime
    val eighthTime = beatTime/2
    val sixteenthTime = beatTime/4

    this.time.toInt match {
      case `wholeTime` => "Semibreve"
      case `halfTime` => "Minima"
      case `quarterTime` => "Semiminima"
      case `eighthTime` => "Croma"
      case `sixteenthTime` => "Semicroma"
      case _ => "Nunno sacciu"
    }
  }

}
