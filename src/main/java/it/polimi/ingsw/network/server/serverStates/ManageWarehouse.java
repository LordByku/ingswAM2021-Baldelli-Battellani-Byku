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

public class ManageWarehouse extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line).getAsJsonObject();

        String command = ServerParser.getInstance().getCommand(json);

        switch (command) {
            case "addToDepot": {
                int depotIndex = json.get("depotIndex").getAsInt();
                ConcreteResourceSet toAdd = ServerParser.getInstance().getConcreteResourceSet(json.getAsJsonObject("set"));

                if(Controller.getInstance().addResourcesToDepot(clientHandler.getPerson(), toAdd, depotIndex)) {
                    Consumer<GameStateSerializer> lambda = (serializer) -> {
                        serializer.addWarehouse(clientHandler.getPerson());
                    };

                    clientHandler.updateGameState(lambda);

                    JsonObject outMessage = new JsonObject();
                    outMessage.add("obtained", ServerParser.getInstance().serialize(Controller.getInstance().concreteToObtain()));
                    clientHandler.ok("confirm", outMessage);
                } else {
                    clientHandler.error("Invalid Choice");
                }

                break;
            }
            case "removeFromDepot": {
                int depotIndex = json.get("depotIndex").getAsInt();
                ConcreteResourceSet toRemove = ServerParser.getInstance().getConcreteResourceSet(json.getAsJsonObject("set"));

                if(Controller.getInstance().removeResourcesFromDepot(clientHandler.getPerson(), toRemove, depotIndex)) {
                    Consumer<GameStateSerializer> lambda = (serializer) -> {
                        serializer.addWarehouse(clientHandler.getPerson());
                    };

                    clientHandler.updateGameState(lambda);

                    JsonObject outMessage = new JsonObject();
                    outMessage.add("obtained", ServerParser.getInstance().serialize(Controller.getInstance().concreteToObtain()));
                    clientHandler.ok("confirm", outMessage);
                } else {
                    clientHandler.error("Invalid Choice");
                }
                break;
            }
            case "swapFromDepots": {
                int depotIndexA = json.get("depotIndexA").getAsInt();
                int depotIndexB = json.get("depotIndexB").getAsInt();

                if(Controller.getInstance().swapResourcesFromDepots(clientHandler.getPerson(), depotIndexA, depotIndexB)) {
                    Consumer<GameStateSerializer> lambda = (serializer) -> {
                        serializer.addWarehouse(clientHandler.getPerson());
                    };

                    clientHandler.updateGameState(lambda);

                    JsonObject outMessage = new JsonObject();
                    outMessage.add("obtained", ServerParser.getInstance().serialize(Controller.getInstance().concreteToObtain()));
                    clientHandler.ok("confirm", outMessage);
                } else {
                    clientHandler.error("Invalid Choice");
                }
                break;
            }
            case "confirmWarehouse": {
                Controller.getInstance().confirmWarehouse(clientHandler.getPerson());
                Consumer<GameStateSerializer> lambda = (serializer) -> {
                    serializer.addMarbleMarket();
                    for(Player player: Game.getInstance().getPlayers()) {
                        serializer.addFaithTrack((Person) player);
                    }
                };

                clientHandler.updateGameState(lambda);

                clientHandler.setState(new EndTurn());
                break;
            }
            default: {
                clientHandler.error("Invalid Command");
            }
        }
    }
}
