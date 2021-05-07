package it.polimi.ingsw.network.server.serverStates;

import it.polimi.ingsw.network.server.ClientHandler;

public abstract class ServerState {
    public abstract void handleClientMessage(ClientHandler clientHandler, String line);
}
