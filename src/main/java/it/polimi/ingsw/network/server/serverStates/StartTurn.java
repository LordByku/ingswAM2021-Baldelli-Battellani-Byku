package it.polimi.ingsw.network.server.serverStates;

import it.polimi.ingsw.network.server.ClientHandler;

public class StartTurn extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        if(clientHandler.getPerson().isActivePlayer()) {
            // TODO: handle message
        } else {
            clientHandler.error("Not your turn");
        }
    }
}
