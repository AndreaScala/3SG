class Note (var pitch: Float, var time: Float) {
  override def toString: String =
    s"(Altezza MIDI: $pitch, Durata: $time)"
}
