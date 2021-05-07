package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.ServerParser;

public class InitDiscard extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line);

        String command = ServerParser.getInstance().getCommand(json);

        if(command.equals("initDiscard")) {
            int[] indices = ServerParser.getInstance().parseIntArray(json.getAsJsonArray("value"));

            if(Controller.getInstance().initDiscard(clientHandler.getPerson(), indices)) {
                clientHandler.ok("confirm", null);

                // TODO: handle next state transition
            } else {
                clientHandler.error("Invalid choice");
            }
        } else {
            clientHandler.error("Invalid command");
        }
    }
}
