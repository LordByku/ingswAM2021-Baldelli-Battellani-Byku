# Prova Finale Ingegneria del Software 2020/2021
AM24 - Baldelli Battellani Byku


## Sviluppatori

[Emanuele Baldelli](https://github.com/emadens)

[Paolo Battellani](https://github.com/paolob2)

[Andrea Byku](https://github.com/LordByku)

## Funzionalità implementate

* Regole Complete
* CLI + GUI
* Socket
* _3 Funzionalità Avanzate:_
  * Partita in locale
  * Resistenza alle disconnessioni
  * Editor dei parametri

## Istruzioni per l'esecuzione

### Server
Per lanciare il server posizionarsi nella directory /deliverables/final/AM24_jar e lanciare da terminale il seguente comando:

bash

java -jar AM24.jar server _[port]_

### Client
Per lanciare il client posizionarsi nella directory /deliverables/final/AM24_jar e lanciare da terminale il comando corrispondente alla modalità:

#### CLI
Per giocare con la CLI:

bash

java -jar AM24.jar client _[hostname]_ _[port]_ cli

#### GUI
Per giocare con la GUI:

bash

java -jar AM24.jar client _[hostname]_ _[port]_ gui

### Editor
Per lanciare l'editor dei parametri posizionarsi nella directory /deliverables/final/AM24Editor_jar e lanciare da terminale il seguente comando:

bash

java -jar AM24.jar
