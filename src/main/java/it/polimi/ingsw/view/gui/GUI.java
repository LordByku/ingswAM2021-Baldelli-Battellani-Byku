package it.polimi.ingsw.view.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.gui.windows.GUIWindow;
import it.polimi.ingsw.view.gui.windows.InitWindow;

import javax.swing.*;
import java.util.ArrayList;

public class GUI implements ViewInterface {
    private final JFrame frame;
    private final Client client;
    private GUIWindow guiWindow;

    public GUI(Client client) {
        this.client = client;
        frame = new JFrame("Masters of Renaissance");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiWindow = new InitWindow(client, frame);
    }

    @Override
    public void onError(Client client, String message) {

    }

    @Override
    public void onCommand(Client client, String player, CommandBuffer commandBuffer) {

    }

    @Override
    public void onUpdate(Client client) {

    }

    @Override
    public void onUserInput(Client client, String line) {

    }

    @Override
    public void onUnexpected(Client client) {

    }

    @Override
    public void onEndGame(Client client, JsonObject endGameMessage) {

    }

    @Override
    public void init(Client client) {
    }

    @Override
    public void welcome(Client client) {

    }

    @Override
    public void loadGame(Client client) {

    }

    @Override
    public void updatePlayerList(Client client, ArrayList<String> nicknames, String hostNickname) {
        // TODO: player lobby
    }

    @Override
    public void startGame(Client client, String line) {

    }
}
