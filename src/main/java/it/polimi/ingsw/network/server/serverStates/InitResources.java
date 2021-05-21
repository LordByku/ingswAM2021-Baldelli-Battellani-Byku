package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.network.server.ServerParser;

import java.util.function.Consumer;

public class InitResources extends ServerState {
    public InitResources() {}

    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line).getAsJsonObject();

        String command = ServerParser.getInstance().getCommand(json);

        if(command.equals("initResources")) {
            ConcreteResourceSet[] warehouse = ServerParser.getInstance().parseConcreteResourceSetArray(json.getAsJsonArray("value"));

            if(Controller.getInstance().initResources(clientHandler.getPerson(), warehouse)) {
                boolean completed = true;
                for(Player player: Game.getInstance().getPlayers()) {
                    Person person = (Person) player;
                    if(person.isConnected() && !Controller.getInstance().hasInitSelected(person)) {
                        completed = false;
                    }
                }
                if(completed) {
                    clientHandler.advanceAllStates(StartTurn::new);
                }

                Consumer<GameStateSerializer> lambda = (serializer) -> {
                    serializer.addWarehouse(clientHandler.getPerson());
                };

                clientHandler.updateGameState(lambda);
            } else {
                clientHandler.error("Invalid choice");
            }
        } else {
            clientHandler.error("Invalid command");
        }
    }
}
