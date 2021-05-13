package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.network.server.ServerParser;

import java.util.function.Consumer;

public class EndTurn extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line).getAsJsonObject();
        String command = ServerParser.getInstance().getCommand(json);

        switch(command) {
            case "endTurn": {
                break;
            }
            case "leaderCard": {
                JsonObject message = ServerParser.getInstance().getMessage(json).getAsJsonObject();
                int leaderCardIndex = message.get("leaderCard").getAsInt();

                switch(message.get("action").getAsString()) {
                    case "play": {
                        if(Controller.getInstance().playLeaderCard(clientHandler.getPerson(), leaderCardIndex)) {
                            Consumer<GameStateSerializer> lambda = (serializer) -> {
                                serializer.addHandLeaderCards(clientHandler.getPerson());
                                serializer.addPlayedLeaderCards(clientHandler.getPerson());
                                serializer.addWarehouse(clientHandler.getPerson());
                            };

                            clientHandler.updateGameState(lambda);
                        } else {
                            clientHandler.error("Invalid choice");
                        }
                        break;
                    }
                    case "discard": {
                        if(Controller.getInstance().discardLeaderCard(clientHandler.getPerson(), leaderCardIndex)) {
                            Consumer<GameStateSerializer> lambda = (serializer) -> {
                                serializer.addHandLeaderCards(clientHandler.getPerson());
                                serializer.addFaithTrack(clientHandler.getPerson());
                            };

                            clientHandler.updateGameState(lambda);
                        } else {
                            clientHandler.error("Invalid choice");
                        }
                        break;
                    }
                    default: {
                        clientHandler.error("Invalid Leader Card Action");
                    }
                }
                break;
            }
            default: {
                clientHandler.error("Invalid command");
            }
        }
    }
}
