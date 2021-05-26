package it.polimi.ingsw.view;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.network.client.Client;

import java.util.ArrayList;

public interface ViewInterface {
    void onError(Client client, String message);

    void onCommand(Client client, String player, CommandBuffer commandBuffer);

    void onUpdate(Client client);

    void onUserInput(Client client, String line);

    void onUnexpected(Client client);

    void onEndGame(Client client, JsonObject endGameMessage);

    void init(Client client);

    void selectMode(Client client);

    void loadGame(Client client);

    void updatePlayerList(Client client, ArrayList<String> nicknames, String hostNickname);

    void startGame(Client client, String line);
}
