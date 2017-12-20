#Installazione Package Necessari e Inclusione

if (!require("dplyr")) install.packages("dplyr")
if (!require("audio")) install.packages("audio")
library("dplyr")
library("audio")

#Legge il file di testo, restituisce un oggetto con dentro l'insieme di pitch (altezza delle note) e duration (durata delle note)
#Formato delle righe del file di testo: DURATA NOTA OTTAVA (es: Semiminima FA# 4)
getScore <- function(scoreString, isFirstLineBpm = TRUE) {
    notelist <- strsplit(scoreString, "\n")[[1]]
    pitchlist <- c()
    durationlist <- c()
    if (isFirstLineBpm) { #Estrapola i bpm se sono la prima riga della lista di stringhe passata
        bpm <- strsplit(notelist[1], " ")[[1]][2]
        bpm <- strtoi(bpm)
        notelist <- notelist[-1]
    }
    #Per ogni linea del file di testo estrapola nome della nota e calcola valore durata, quindi li aggiunge in coda a due rispettive liste
    for (elem in notelist) {
        mynote <- strsplit(elem, " ")[[1]]
        mypitch <- paste(mynote[length(mynote) - 1], mynote[length(mynote)], sep = "") #Si uniscono NOTA e OTTAVA (es. MI 6 diventa MI6)
        pitchlist <- c(pitchlist, mypitch)
        pointnum <- 2
        switch(mynote[1], #Si convertono i nomi dei valori delle note in valori di duration, secondo la mappatura fatta nello switch
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
        durationlist <- c(durationlist, number)
    }
    result <- list(pitchList = pitchlist, durationList = durationlist, bpm = bpm)
    return (result)
}

#la lunga stringa scoreString (una stringifizzazione dello spartito) viene passata come parametro dal programma SCALA
score <- getScore(scoreString)
mynotelist <- score$noteList

#Tipo enumerativo per la corrispondenza fra i nomi delle note ed il valore intero corrispondente
notes <- c(LA = 0, SI = 2, DO = 3, RE = 5, MI = 7, FA = 8, SOL = 10)

song <- data_frame(pitch = score$pitchList, duration = score$durationList)
#Utilizzo del valore di pitch per trovare la frequenza della sinusoide corrispondente
song <-
    song %>%
    mutate(octave = substring(pitch, nchar(pitch)) %>% { suppressWarnings(as.numeric(.)) } %>%
           ifelse(is.na(.), 4, .),
           #Estrapolazione carattere corretto a seconda che la nota sia diesis o meno
         note = ifelse(grepl("#", pitch), notes[substr(pitch, 1, nchar(pitch) - 2)], notes[substr(pitch, 1, nchar(pitch) - 1)]),
         #Aumento frequenza in caso di nota diesis
         note = note + grepl("#", pitch) + octave * 12 + 12 * (note < 3),
           #Formula per definire il valore in Hz della sinusoide dato il valore numerico trovato
		 freq = 2 ^ ((note - 60) / 12) * 440)
tempo <- score$bpm
#Frequenza standard di campionamento
sample_rate <- 44100


#Funzione per la generazione dell'onda sinusoidale
make_sine <- function(freq, duration) {
	wave <- sin(seq(0, duration / tempo * 60, 1 / sample_rate) *
                freq * 2 * pi)
    #Piccolo fading aggiunto al termine di ogni nota per evitare un suono altrimenti disturbato, proporzionale ai BPM
	fade <- seq(0, 1, tempo / (3*sample_rate))
	wave * c(fade, rep(1, length(wave) - 2 * length(fade)), rev(fade))
}

#Applicazione della funzione a tutte le note analizzate
song_wav <-
    mapply(make_sine, song$freq, song$duration) %>%
    do.call("c", .)
#Riproduzione
play(song_wav)
