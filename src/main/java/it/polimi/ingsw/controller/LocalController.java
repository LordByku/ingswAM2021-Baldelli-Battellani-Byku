package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.GameStateSerializer;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class LocalController implements Runnable {
    private final Client client;
    private final BlockingQueue<String> readBuffer;
    private final BlockingQueue<String> writeBuffer;
    private CommandBuffer commandBuffer;

    public LocalController(Client client, BlockingQueue<String> readBuffer, BlockingQueue<String> writeBuffer) {
        commandBuffer = null;
        this.client = client;
        this.readBuffer = readBuffer;
        this.writeBuffer = writeBuffer;
    }

    public void sendEndGameMessage() {
        JsonObject endGameMessage = new JsonObject();

        JsonArray jsonArray = new JsonArray();
        Person person = Game.getInstance().getSinglePlayer();
        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("player", person.getNickname());
        playerObject.addProperty("basePoints", person.getBoard().getPoints());
        playerObject.addProperty("resources", person.getBoard().getResources().size());

        jsonArray.add(playerObject);

        endGameMessage.add("results", jsonArray);
        endGameMessage.addProperty("computerWin", Game.getInstance().getComputer().hasWon());

        ok("endGame", endGameMessage);
    }

    public void handleUserMessage(String message) {
        if(Game.getInstance().hasGameEnded()) {
            sendEndGameMessage();
            return;
        }

        JsonObject json = JsonUtil.getInstance().parseLine(message).getAsJsonObject();
        String request = json.get("request").getAsString();

        Person person = Game.getInstance().getSinglePlayer();

        switch (request) {
            case "newCommand": {
                // client requests a new command buffer
                JsonElement commandElement = json.get("command");
                CommandType commandType = Deserializer.getInstance().getCommandType(commandElement);
                try {
                    commandBuffer = commandType.getCommandBuffer(person);
                    JsonObject commandObject = JsonUtil.getInstance().serializeCommandBuffer(commandBuffer, person);
                    ok("command", commandObject);
                } catch (InvalidCommandException e) {
                    error("Invalid request");
                }

                break;
            }
            case "cancel": {
                // client cancels the current command buffer
                Consumer<GameStateSerializer> lambda;
                if (commandBuffer != null && !commandBuffer.isCompleted() && (lambda = commandBuffer.cancel()) != null) {
                    commandBuffer = null;

                    updateGameState(lambda);

                    JsonObject commandObject = JsonUtil.getInstance().serializeCommandBuffer(commandBuffer, person);
                    ok("command", commandObject);
                } else {
                    error("Invalid request");
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
                        updateGameState(lambda);
                    }

                    JsonObject commandObject = JsonUtil.getInstance().serializeCommandBuffer(commandBuffer, person);
                    ok("command", commandObject);
                } else {
                    error("Invalid request");
                }

                break;
            }
            case "endTurn": {
                // client ends the turn
                if (person.isActivePlayer() && person.mainAction()) {
                    person.endTurn();

                    if(Game.getInstance().hasGameEnded()) {
                        sendEndGameMessage();
                    } else {
                        Consumer<GameStateSerializer> lambda = (serializer) -> {
                            serializer.addCardMarket();
                            serializer.addFaithTrack(person);
                            serializer.addFlippedActionToken();
                        };

                        updateGameState(lambda);

                        ok("command", JsonUtil.getInstance().serializeCommandBuffer(commandBuffer, person));
                    }
                } else {
                    error("Invalid request");
                }

                break;
            }
            default: {
                error("Invalid request");
            }
        }
    }

    private void ok(String type, JsonObject message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "ok");
        jsonMessage.addProperty("type", type);
        jsonMessage.add("message", message);

        try {
            writeBuffer.put(jsonMessage.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void error(String message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "error");
        jsonMessage.addProperty("message", message);

        try {
            writeBuffer.put(jsonMessage.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateGameState(Consumer<GameStateSerializer> lambda) {
        Person person = Game.getInstance().getSinglePlayer();
        GameStateSerializer serializer = new GameStateSerializer(person.getNickname());
        lambda.accept(serializer);
        ok("update", serializer.getMessage());
    }

    @Override
    public void run() {
        JsonObject commandObject = JsonUtil.getInstance().serializeCommandBuffer(commandBuffer, Game.getInstance().getSinglePlayer());
        ok("command", commandObject);

        try {
            while (true) {
                handleUserMessage(readBuffer.take());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
