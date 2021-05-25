package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameAlreadyStartedException;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.parsing.Parser;

public abstract class ServerState {
    public abstract void handleClientMessage(ClientHandler clientHandler, String line);

    protected JsonObject getConfigMessage() throws GameAlreadyStartedException {
        JsonObject jsonMessage = new JsonObject();
        JsonArray playerOrder = new JsonArray();

        if (Game.getInstance().getNumberOfPlayers() == 1) {
            playerOrder.add(Game.getInstance().getSinglePlayer().getNickname());
        } else {
            for (Player player : Game.getInstance().getPlayers()) {
                playerOrder.add(((Person) player).getNickname());
            }
        }

        JsonObject config = Parser.getInstance().getConfig();
        jsonMessage.add("config", config);
        jsonMessage.add("turnOrder", playerOrder);
        return jsonMessage;
    }

    public abstract void forceTermination(ClientHandler clientHandler);
}
