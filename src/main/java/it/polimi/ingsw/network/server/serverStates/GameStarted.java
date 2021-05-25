package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.InvalidCommandException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.utility.JsonUtil;

import java.util.function.Consumer;

public class GameStarted extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        CommandBuffer commandBuffer = clientHandler.getCommandBuffer();

        JsonObject json = JsonUtil.getInstance().parseLine(line).getAsJsonObject();
        String request = json.get("request").getAsString();

        switch (request) {
            case "newCommand": {
                // client requests a new command buffer
                JsonElement commandElement = json.get("command");
                CommandType commandType = Deserializer.getInstance().getCommandType(commandElement);
                try {
                    clientHandler.setBuffer(commandType.getCommandBuffer(clientHandler.getPerson()));
                    System.out.println("New buffer successfully created");
                    JsonObject commandObject = JsonUtil.getInstance().serializeCommandBuffer(clientHandler.getCommandBuffer(), clientHandler.getPerson());
                    clientHandler.broadcast("command", commandObject);
                } catch (InvalidCommandException e) {
                    clientHandler.error("Invalid request");
                }

                break;
            }
            case "cancel": {
                // client cancels the current command buffer
                Consumer<GameStateSerializer> lambda;
                if (commandBuffer != null && !commandBuffer.isCompleted() && (lambda = commandBuffer.cancel()) != null) {
                    clientHandler.setBuffer(null);

                    clientHandler.updateGameState(lambda);

                    JsonObject commandObject = JsonUtil.getInstance().serializeCommandBuffer(clientHandler.getCommandBuffer(), clientHandler.getPerson());
                    clientHandler.broadcast("command", commandObject);
                } else {
                    clientHandler.error("Invalid request");
                }

                break;
            }
            case "action": {
                // client executes an action on the current command buffer
                if (commandBuffer != null && !commandBuffer.isCompleted()) {
                    String command = json.get("command").getAsString();
                    JsonElement value = json.get("value");

                    Consumer<GameStateSerializer> lambda = commandBuffer.handleMessage(command, value);

                    if (lambda != null) {
                        clientHandler.updateGameState(lambda);
                    }

                    JsonObject commandObject = JsonUtil.getInstance().serializeCommandBuffer(clientHandler.getCommandBuffer(), clientHandler.getPerson());
                    clientHandler.broadcast("command", commandObject);
                } else {
                    clientHandler.error("Invalid request");
                }

                break;
            }
            case "endTurn": {
                // client ends the turn
                Person person = clientHandler.getPerson();
                if (person.isActivePlayer() && person.mainAction()) {
                    if (Game.getInstance().getNumberOfPlayers() == 1) {
                        Consumer<GameStateSerializer> lambda = (serializer) -> {
                            serializer.addCardMarket();
                            serializer.addFaithTrack(person);
                            serializer.addFlippedActionToken();
                        };

                        clientHandler.updateGameState(lambda);
                    }

                    clientHandler.endTurn();
                } else {
                    clientHandler.error("Invalid request");
                }

                break;
            }
            default: {
                clientHandler.error("Invalid request");
            }
        }
    }

    @Override
    public void forceTermination(ClientHandler clientHandler) {

    }
}
