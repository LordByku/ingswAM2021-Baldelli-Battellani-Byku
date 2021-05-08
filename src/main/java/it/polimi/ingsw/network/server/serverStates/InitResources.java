package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.network.server.ServerParser;

import java.util.ArrayList;
import java.util.function.Consumer;

public class InitResources extends ServerState {
    private static final ArrayList<ClientHandler> completed = new ArrayList<>();

    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line);

        String command = ServerParser.getInstance().getCommand(json);

        if(command.equals("initResources")) {
            ConcreteResourceSet[] warehouse = ServerParser.getInstance().parseConcreteResourceSetArray(json.getAsJsonArray("value"));

            if(Controller.getInstance().initResources(clientHandler.getPerson(), warehouse)) {
                synchronized (completed) {
                    completed.add(clientHandler);
                    if(completed.size() == Game.getInstance().getNumberOfPlayers()) {
                        for(ClientHandler completedClientHandler: completed) {
                            completedClientHandler.setState(new StartTurn());
                        }
                    }
                }

                Consumer<GameStateSerializer> lambda = (serializer) -> {
                    serializer.addWarehouse(clientHandler.getPerson());
                };

                clientHandler.updateState(lambda);
            } else {
                clientHandler.error("Invalid choice");
            }
        } else {
            clientHandler.error("Invalid command");
        }
    }
}
