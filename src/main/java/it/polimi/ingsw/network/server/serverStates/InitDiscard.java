package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.ServerParser;

import java.util.ArrayList;

public class InitDiscard extends ServerState {
    private static final ArrayList<ClientHandler> completed = new ArrayList<>();

    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line);

        String command = ServerParser.getInstance().getCommand(json);

        if(command.equals("initDiscard")) {
            int[] indices = ServerParser.getInstance().parseIntArray(json.getAsJsonArray("value"));

            if(Controller.getInstance().initDiscard(clientHandler.getPerson(), indices)) {
                synchronized (completed) {
                    completed.add(clientHandler);
                    if(completed.size() == Game.getInstance().getNumberOfPlayers()) {
                        for(ClientHandler completedClientHandler: completed) {
                            completedClientHandler.setState(new InitResources());
                        }
                    }
                }

                // TODO: broadcast updated gameState
            } else {
                clientHandler.error("Invalid choice");
            }
        } else {
            clientHandler.error("Invalid command");
        }
    }
}
