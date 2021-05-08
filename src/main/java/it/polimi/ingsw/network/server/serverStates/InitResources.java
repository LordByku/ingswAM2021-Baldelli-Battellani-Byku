package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.ServerParser;

import java.util.ArrayList;

public class InitResources extends ServerState {
    private static final ArrayList<ClientHandler> completed = new ArrayList<>();

    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line);

        String command = ServerParser.getInstance().getCommand(json);

        if(command.equals("initDiscard")) {
            ConcreteResourceSet[] warehouse = ServerParser.getInstance().parseConcreteResourceSetArray(json.getAsJsonArray("value"));

            if(Controller.getInstance().initResources(clientHandler.getPerson(), warehouse)) {
                synchronized (completed) {
                    completed.add(clientHandler);
                    if(completed.size() == Game.getInstance().getNumberOfPlayers()) {
                        // TODO: handle next state transition and assign faith points
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
