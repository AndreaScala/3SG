# Title     : Tipographe.R
# Objective : Usare le capabilities grafiche di R per creare immagini che simulino partiture

#injection di collezioni pitchList, timeList sotto forma di stringhe e tempo

noteList <- strsplit(noteList, " ")[[1]]
timeList <- as.numeric(strsplit(timeList, " ")[[1]])
figureList <- strsplit(figureList, " ")[[1]]
tempo <- as.numeric(tempo)

absoluteTimeList <- c(0)
naturalNotesList <- c()
totalTime <-0

naturalNotes <- c(DO = 0, RE = 1, MI = 2, FA = 3, SO = 4, LA = 5, SI = 6) #SO non è SOL perchè è univocamente individuato dalle prime due lettere
noteValues <- c(Semibreve = 4, Minima = 2, Semiminima = 1, Croma = 0.5, Semicroma = 0.25, Biscroma = 0.125, Semibiscroma = 0.0625)

#Creazione vettore delle ascisse delle note (absoluteTimeList) e delle linee di fine battuta (barMarker)
barSaturator <- 0
barMarkersList <- c()
for (time in timeList) {
    totalTime <- totalTime + time
    barSaturator <- barSaturator + time
    if (barSaturator==tempo) {
        barMarkersList <- c(barMarkersList, totalTime + 0.5)
        totalTime <- totalTime + 1
        barSaturator <-0
    }
    absoluteTimeList <- c(absoluteTimeList, totalTime)
}
absoluteTimeList <- head(absoluteTimeList, -1)

#Creazione vettore delle ordinate delle note
for (note in noteList) {
    #considera i primi due caratteri e l'ultimo carattere per mappare la nota su un numero
    #es. RE4 oppure SOL#5
    naturalNotesList <- c(
        naturalNotesList,
        naturalNotes[[substr(note, 1, 2)]] + 8*as.numeric(substr(note, nchar(note), nchar(note)))
    )
}

#Interpretazione comandi battuta iniziale e finale (xlimit): se entrambi i comandi vuoti stampa normale, se solo un
# comando stampa da 1 a battuta, se entrambi validi stampa da battuta1 a battuta2
if (nchar(xlimit)==0) {
    xlimit <- c(NULL, NULL)
} else {
    xlimit <- strsplit(xlimit, " ")[[1]]
    if (length(xlimit)==1) {
        if (!(as.numeric(xlimit) %in% 2:length(barMarkersList))) xlimit <- c(NULL, NULL)
        else xlimit <- c (1, barMarkersList[as.numeric(xlimit)])
    } else{
        if (!(as.numeric(xlimit[1]) %in% 2:length(barMarkersList) && as.numeric(xlimit[2]) %in% 2:length(barMarkersList))) xlimit <- c(NULL, NULL)
        else xlimit <- c(barMarkersList[as.numeric(xlimit[1])-1], barMarkersList[as.numeric(xlimit[2])])
        print(xlimit)
    }
}

#DISEGNO NOTE SPARTITO(plot)
#Di default vengono disegnate tante note con notazione circolare, che corrispondono alle Semibrevi.
#Sta poi al programma leggere un vettore di nomi di valori (semibreve, minima puntata) e man mano modificare
#il disegno delle note che passa in rassegna. Creato attraverso if in cascata

plot(absoluteTimeList, naturalNotesList, main = "Spartito Automatico", subtitle = "Tipographe.R", xlab = "", ylab = "", xlim=xlimit)

j <- 1
for (i in 1:length(naturalNotesList)) {
    if (noteValues[[figureList[j]]] <= noteValues[["Minima"]])
        segments(
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) -0.05 else +0.05,
        naturalNotesList[i],
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) -0.05 else +0.05,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -1 else +1
        )
    if (noteValues[[figureList[j]]] <= noteValues[["Semiminima"]])
        points(absoluteTimeList[i], naturalNotesList[i], pch = 19)
    if (noteValues[[figureList[j]]] <= noteValues[["Croma"]])
        segments(
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) -0.05 else +0.05,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -1 else +1,
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) 0.1 else +0.2,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -0.4 else +0.4
        )
    if (noteValues[[figureList[j]]] <= noteValues[["Semicroma"]])
        segments(
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) -0.05 else +0.05,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -0.9 else +0.9,
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) 0.1 else +0.2,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -0.3 else +0.3
        )
    if (noteValues[[figureList[j]]] <= noteValues[["Biscroma"]])
        segments(
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) -0.05 else +0.05,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -0.8 else +0.8,
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) 0.1 else +0.2,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -0.2 else +0.2
        )
    if (noteValues[[figureList[j]]] <= noteValues[["Semibiscroma"]])
        segments(
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) -0.05 else +0.05,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -0.7 else +0.7,
        absoluteTimeList[i] + if (naturalNotesList[i]>=30) 0.1 else +0.2,
        naturalNotesList[i] + if (naturalNotesList[i]>=30) -0.1 else +0.1
        )

    valuePointCounter <- 1
    while (j+1 <= length(figureList) && figureList[j+1] == "Puntata") {
        points(absoluteTimeList[i] + 0.15*valuePointCounter, naturalNotesList[i], pch = 20)
        j <- j+1
        valuePointCounter <- valuePointCounter +1
    }
    j <- j+1
    valuePointCounter <- 1
}

#DISEGNO STANGHETTA per le note fuori dal pentagramma
for (i in 1:length(naturalNotesList)){
    if (naturalNotesList[i]<= 24 || naturalNotesList[i] >= 36){
        x <- absoluteTimeList[i]
        y <- naturalNotesList[i]
        if (y <= 24) {
            if (y%%2!=0) y<- y+1
            sequence <- seq(y, 24, 2)
        }
        if (naturalNotesList[i] >= 34){
            if (y%%2!=0) y <- y - 1
            sequence <- seq(34, y, 2)
        }

        for (j in sequence)
            segments(x-0.2, j, x+0.2, j)

    }
}

#DISEGNO DIESIS
for (i in 1:length(noteList))
    if (substr(noteList[i], nchar(noteList[i])-1, nchar(noteList[i])-1) == "#")
        points(absoluteTimeList[i] - 0.1, naturalNotesList[i], pch = 9)




#DISEGNO LINEE SPARTITO (Pentagramma & Linea di Battuta)
abline (v= barMarkersList, h=seq(26, 34, 2), col = "gray")
