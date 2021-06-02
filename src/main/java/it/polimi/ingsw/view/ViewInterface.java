package it.polimi.ingsw.view;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;

import java.util.ArrayList;

public interface ViewInterface {
    void onFatalError(String message);

    void onError(String message);

    void onCommand(String player, CommandBuffer commandBuffer);

    void onUpdate();

    void onUserInput(String line);

    void onUnexpected();

    void onEndGame(JsonObject endGameMessage);

    void init();

    void welcome();

    void loadGame();

    void updatePlayerList(ArrayList<String> nicknames, String hostNickname);

    void startGame(String line);

    void startConnection();

    void connectionFailed(int timerDelay);

    void terminate();

    void join();

    void loadResources();
}
