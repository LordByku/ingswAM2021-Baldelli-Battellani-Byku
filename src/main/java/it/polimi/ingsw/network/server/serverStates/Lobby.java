package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameAlreadyStartedException;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.ServerParser;
import it.polimi.ingsw.parsing.Parser;

public class Lobby extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        try {
            JsonObject clientMessage = ServerParser.getInstance().parseLine(line).getAsJsonObject();

            if(clientHandler.getPerson().isHost()){
                if (ServerParser.getInstance().getCommand(clientMessage).equals("startGame")) {
                    if (Game.getInstance().getNumberOfPlayers() >= 2) {
                        Game.getInstance().startMultiPlayer();

                        JsonObject jsonMessage = new JsonObject();
                        JsonArray playerOrder = new JsonArray();
                        for (Player player : Game.getInstance().getPlayers()) {
                            playerOrder.add(((Person) player).getNickname());
                        }

                        JsonObject config = Parser.getInstance().getConfig();
                        jsonMessage.add("config", config);
                        jsonMessage.add("turnOrder", playerOrder);
                        clientHandler.broadcast("config", jsonMessage);

                        clientHandler.startGame();
                    } else {
                        clientHandler.error("Not enough players to start the game");
                    }
                } else {
                    clientHandler.error("Invalid command");
                }
            } else {
                clientHandler.error("Not the host");
            }
        } catch (GameAlreadyStartedException e) {
            clientHandler.error("Game has already started");
        }
    }
}
