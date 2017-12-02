SCALA's Scale by Scala and Gerloni (3SG)

Il presente applicativo scritto in linguaggio SCALA permette l'analisi statistica di una partitura musicale.
Il programma accetta come input un file di testo derivante dall'analisi di un file MIDI esportato da programmi di editing per partiture
(fra i pi� famosi ricordiamo Finale Notepad e GuitarPro).
Il file di testo viene generato tramite una patch scritta in linguaggio Pure Data che, tramite meccanismi di analisi MIDI ed FFT, scrive sul file
di testo l'elenco delle note rilevate, nel seguente formato:

<pitch>t<duration>;

Dove pitch � un intero rappresentante l'altezza della nota in formato MIDI e duration un float rappresentante la durata in millisecondi.
Il file di testo cos� ottenuto (nel repository sono presenti 3 file di esempio) � dato come input all'applicativo SCALA.
Il programma richiede come input il nome del file e i bpm dello stesso. Dopodich� richiede quali analisi effettuare.
Fra le funzionalit� abbiamo:
1 - Interpretazione delle note e traduzione dal formato MIDI ad un formato "umanamente comprensibile".
2 - Rilevazione Ottava Centrale (l'ottava su cui il brano si concentra maggiormente)
3 - Rilevazione Tonalit� Brano.
4 - Rilevazione Tempo Brano.

Tutte le funzionalit� sono implementate utilizzando algoritmi di Pattern Matching uniti a formule euristiche e algoritmi statistici applicati alla
teoria musicale. Pertanto non sempre i risultati ottenuti sono perfettamente conformi alla realt�. Si � riuscito tuttavia ad ottenere un elevato
grado di fedelt�. Si riescono infatti ad interpretare senza problemi tutte le note anche di brani piuttosto complessi, compresi quelli con
caratteristiche inusuali, come il terzo file d'esempio.

I tre file d'esempio sono:
1 - We Wish You a Merry Christmas (bpm usati: 120). Piccolo e semplice brano di prova utilizzato come test nelle fasi iniziali dello sviluppo.
Tonalit� effettiva: SOL Maggiore (tutti i FA sono diesis (#)). Tempo effettivo: 3/4.
2 - Marcia alla Turca (bpm usati: 130). Celebre rond� di Mozart, brano sufficientemente veloce e complesso.
Tonalit� effettiva: LA Maggiore. Tempo effettivo: 4/4.
3 - Take Five (bmp usati: 156). Celebre assolo di tromba Jazz di Dave Brubeck (quello della pubblicit� della Banca Mediolanum). Questo brano �
stato usato nelle fasi finali dello sviluppo. Presenta fraseggi e tempi molto inusuali (nel Jazz � tipica l'improvvisazione e la completa libert�
espressiva, a differenza dei canoni imposti dalla musica classica).
Tonalit� effettiva: SI Maggiore. Tempo effettivo: 5/4 (tempo ritrovato in pochissimi brani).

NB.: E' possibile interpretare i brani anche con bpm diversi da quelli con il quale sono stati composti. Il risultato sar� una conseguente modifica
della durata delle note. Per esempio: se con i bpm consigliati il brano presenter� una semiminima, raddoppiando i bpm con cui viene interpretata
(di fatto raddoppia la velocit� del brano) la stessa nota verr� interpretata come minima (avente durata il doppio di una semiminima).

Andrea Scala
Ilario Gabriele Gerloni