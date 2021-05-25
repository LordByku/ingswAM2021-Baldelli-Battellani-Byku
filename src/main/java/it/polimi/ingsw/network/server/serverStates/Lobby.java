package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameAlreadyStartedException;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.utility.JsonUtil;

public class Lobby extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        try {
            JsonObject clientMessage = JsonUtil.getInstance().parseLine(line).getAsJsonObject();

            if (clientHandler.getPerson().isHost()) {
                if (clientMessage.get("command").getAsString().equals("startGame")) {
                    if (Game.getInstance().getNumberOfPlayers() >= 1) {
                        if (Game.getInstance().getNumberOfPlayers() == 1) {
                            Game.getInstance().startSinglePlayer();
                        } else {
                            Game.getInstance().startMultiPlayer();
                        }

                        clientHandler.broadcast("config", getConfigMessage());

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

    @Override
    public void forceTermination(ClientHandler clientHandler) {

    }
}
