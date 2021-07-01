# Prova Finale Ingegneria del Software 2020/2021 - Masters of Renaissance
AM24 - Baldelli Battellani Byku


## Developers

[Emanuele Baldelli](https://github.com/emadens)

[Paolo Battellani](https://github.com/paolob2)

[Andrea Byku](https://github.com/LordByku)

## Implemented features

* Complete rules
* CLI + GUI
* Socket
* _3 Advanced features:_
  * Local game
  * Resilience to disconnections
  * Parameter Editor

## Instructions to run the game

### Server
To launch the server open this path (/deliverables/final/AM24_jar) from terminal and run the following command:

bash

java -jar AM24.jar server [_port_]

### Client
To launch the client open this path (/deliverables/final/AM24_jar) from terminal and choose the command to run:

#### CLI
To play with the CLI:

bash

java -jar AM24.jar client [_hostname_] [_port_] cli

#### GUI
To play with the GUI:

bash

java -jar AM24.jar client [_hostname_] [_port_] gui

### Editor
To launch the parameter editor open this path (/deliverables/final/AM24Editor_jar) from terminal and run the following command:

bash

java -jar AM24.jar
