class Note (var pitch: Int, var time: Float) {
  //Per stampe di prova
  override def toString: String =
    s"(Altezza MIDI: $pitch, Durata: $time)"

  //Funzione di Pattern Matching per individuare il nome della nota
  //Alcuni dei pitch corrispondono ai sette gradi principali (do, re, mi...),
  //mentre i rimanenti sono gradi alterati (do diesis, re diesis,..) per
  //riconoscerli si vede il grado precedente: avrà il suo nome e diesis a suffisso
  def getNoteName(pitch: Int): String = {
    pitch%12 match {
      case 0 => "DO"
      case 2 => "RE"
      case 4 => "MI"
      case 5 => "FA"
      case 7 => "SOL"
      case 9 => "LA"
      case 11 => "SI"
      case _  => getNoteName(pitch-1)+"#"
    }
  }

  def getNoteName(): String = {
    getNoteName(this.pitch); //overloading del metodo per ragioni di ricorsione
  }

  //Funzione di Pattern Matching per individuare l'ottava della nota
  //L'ottava di riferimento è calcolata facendo il rapporto tra il pitch e 12 (l'insieme dei semitoni dentro l'ottava) e
  //sottraendo 1 per ragioni di convenzione
  def getOctave(): Int = {
    this.pitch/12 match {
      case x => x-1
    }
  }

  //Funzione di Pattern Matching per identificare il tipo della nota (Figura)
  //Alcune duration corrispondono numericamente a una delle misure fondamentali (semibreve, minima, semiminima, ...).
  //Altrimenti corrispondono a misure puntate (semibreve puntata, minima puntata, ...)
  //Sono state aggiunte delle guard di range per sopperire all'errore di approssimazione dei tempi (sensibilità di 1 microsec)
  def getNoteValue(bpm: Float): String = {
    this.time.toInt.toFloat/(60000/bpm) match { // <- calcola la duration
      case n if n >= 3.9f && n < 4.1f => "Semibreve"
      case n if n >= 1.9f && n < 2.1f => "Minima"
      case n if n >= 0.9f && n < 1.1f => "Semiminima"
      case n if n >= 0.4f && n < 0.6f => "Croma"
      case n if n >= 0.24f && n < 0.26f => "Semicroma"
      case n if n >= 0.12f && n < 0.13f => "Biscroma"
      case n if n >= 0.05f && n < 0.07f => "Semibiscroma"
      case _ => getNoteValue(bpm*2f/3f) + " Puntata"
    }
  }

  def getNoteValue(bpm: Int): String = {
    getNoteValue(bpm.toFloat); //overloading del metodo per ragioni di ricorsione
  }

  //Dato un nome di nota lo converte in un numero compreso tra 0 e 11, il valore del pitch relativo
  def getRelativePitchByNoteName(noteName: String): Int ={
    for (relativePitch <- 0 to 11) {
      if (getNoteName(relativePitch.toInt) == noteName) return relativePitch.toInt
    }
    return -1 //mai raggiunto
  }

}
