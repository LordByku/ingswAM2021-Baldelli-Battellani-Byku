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
  * Parameters Editor

## Instructions to run the game

### Server
To launch the server run:

java -jar AM24.jar server [_port_]

### Client
To launch the client run:

#### CLI
java -jar AM24.jar client [_hostname_] [_port_] cli
> Note: CLI requirements: Linux environment

#### GUI
java -jar AM24.jar client [_hostname_] [_port_] gui
> Note: GUI requirements: 1920x1080 minimum resolution

### Editor
To launch the parameters editor run:

java -jar AM24-editor.jar
> Note: GUI requirements: 1920x1080 minimum resolution
