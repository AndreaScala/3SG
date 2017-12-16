#Installazione Package Necessari
install.packages("dplyr")
install.packages("audio")
#Inclusione
library("dplyr")
library("audio")
#Lettura file di testo proveniente da SCALA
mynotelist <- readLines(con = "turkishmarch.txt")
#Estrazione bpm dal file (si tratta sempre del secondo termine della prima riga)
bpm <- strsplit(mynotelist[1], " ")[[1]][2]
#Conversione bpm da stringa a intero
bpm <- strtoi(bpm)
#Inizializzazione vettore altezze
mypitchlist <- c()
#Inizializzazione vettore durate
mydurationlist <- c()
#Pop del termine (bpm) dalla lista in quanto già acquisito
mynotelist <- mynotelist[-1]
#Iterazione su tutte le note
for (elem in mynotelist) {
    #Divisione dei termini di ogni nota (in ordine: figura(durata), eventuale puntata, nome nota(altezza), eventuale diesis, ottava 
    mynote <- strsplit(elem, " ")[[1]]
    #Formazione del pitch (nome nota concatenato ad ottava)
    mypitch <- paste(mynote[length(mynote) - 1], mynote[length(mynote)], sep = "")
    #Append della nota compattata al vettore di altezze
    mypitchlist <- c(mypitchlist, mypitch)
    #Valore intero (per la durata delle note puntate)
    pointnum <- 2
    #Matching fra nome della figura della nota e intero (indicante il numero di battiti)
    switch(mynote[1],
    Semibreve = {
        number <- 4
    },
    Minima = {
        number <- 2
    },
    Semiminima = {
        number <- 1
    },
    Croma = {
        number <- 0.5
    },
    Semicroma = {
        number <- 0.25
    },
    Biscroma = {
        number <- 0.125
    },
    Semibiscroma = {
    number <- 0.0625
    })
    #Incremento del numero di battiti della nota in base al numero di "puntata" presenti
    while (mynote[pointnum] == "Puntata") {
        number <- number + number / (2*(pointnum-1))
        pointnum <- pointnum + 1
    }
    #Append del numero ricavato al vettore delle durate
    mydurationlist <- c(mydurationlist, number)
}
#Tipo enumerativo per la corrispondenza fra i nomi delle note ed il valore intero corrispondente
notes <- c(LA = 0, SI = 2, DO = 3, RE = 5, MI = 7, FA = 8, SOL = 10)
#Formazione della frame contenente le info estrapolate ed opportunamente convertite
song <- data_frame(pitch = mypitchlist, duration = mydurationlist)
#Utilizzo del valore di pitch per trovare la frequenza della sinusoide corrispondente
song <-
    song %>%
    mutate(octave = substring(pitch, nchar(pitch)) %>% { suppressWarnings(as.numeric(.)) } %>%
           ifelse(is.na(.), 4, .),
           #Estrapolazione carattere corretto a seconda che la nota sia diesis o meno
         note = ifelse(grepl("#", pitch), notes[substr(pitch, 1, nchar(pitch) - 2)], notes[substr(pitch, 1, nchar(pitch) - 1)]),
         #Aumento frequenza in caso di nota diesis
         note = note + grepl("#", pitch)
         # Aumento per l'ottava corrispondente
            + octave * 12 +
           12 * (note < 3),
           #Formula per definire il valore in Hz della sinusoide dato il valore numerico trovato
		 freq = 2 ^ ((note - 60) / 12) * 440)
#Assegnazione velocità brano
tempo <- bpm
#Frequenza standard di campionamento
sample_rate <- 44100
#Funzione per la generazione dell'onda sinusoidale
make_sine <- function(freq, duration) {
	wave <- sin(seq(0, duration / tempo * 60, 1 / sample_rate) *
                freq * 2 * pi)
    #Piccolo fading aggiunto al termine di ogni nota per evitare un suono altrimenti disturbato
	fade <- seq(0, 1, 50 / sample_rate)
	wave * c(fade, rep(1, length(wave) - 2 * length(fade)), rev(fade))
}

#Applicazione della funzione a tutte le note analizzate
song_wav <-
    mapply(make_sine, song$freq, song$duration) %>%
    do.call("c", .)
#Riproduzione
play(song_wav)