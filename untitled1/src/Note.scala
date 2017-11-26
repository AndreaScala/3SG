class Note (var pitch: Int, var time: Float) {
  override def toString: String =
    s"(Altezza MIDI: $pitch, Durata: $time)"

}
