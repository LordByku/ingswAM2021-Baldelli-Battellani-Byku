package it.polimi.ingsw.view.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.gameZone.marbles.MarbleColour;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.GUIClientUserCommunication;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cli.Strings;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;
import it.polimi.ingsw.view.gui.windows.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GUI implements ViewInterface {
    private final JFrame frame;
    private final Client client;
    private final BlockingQueue<String> buffer;
    private final Thread clientUserCommunication;
    private GUIWindow guiWindow;

    public GUI(Client client) {
        this.client = client;
        frame = new JFrame("Masters of Renaissance");

        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            //frame.setUndecorated(true);
            frame.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        });

        buffer = new LinkedBlockingQueue<>();
        clientUserCommunication = new Thread(new GUIClientUserCommunication(client, buffer));
        clientUserCommunication.setDaemon(true);
        clientUserCommunication.start();
    }

    @Override
    public void onFatalError(String message) {
        guiWindow.onError(message);
    }

    @Override
    public void onError(String message) {
        guiWindow.onError(message);
    }

    @Override
    public void onCommand(String player, CommandBuffer commandBuffer) {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onUserInput(String line) {
        // TODO : handle window switches better
        if (line.charAt(0) == '!') {
            String[] lines = Strings.splitLine(line);
            switch (lines[1]) {
                case "cardmarket": {
                    guiWindow.setActive(false, frame);
                    guiWindow = new CardMarketView(client, buffer);
                    guiWindow.setActive(true, frame);
                    break;
                }
                case "marblemarket": {
                    guiWindow.setActive(false, frame);
                    guiWindow = new MarbleMarketView(client, buffer);
                    guiWindow.setActive(true, frame);
                }
            }
        } else {
            System.out.println("sending: " + line);
            client.write(line);
        }
    }

    @Override
    public void onUnexpected() {

    }

    @Override
    public void onEndGame(JsonObject endGameMessage) {

    }

    @Override
    public void init() {
        guiWindow = new Welcome(client, buffer);
        guiWindow.setActive(true, frame);
    }

    @Override
    public void welcome() {

    }

    @Override
    public void loadGame() {
        try {
            for (ResourceImageType resourceImageType : ResourceImageType.values()) {
                resourceImageType.loadImage();
            }
            for (CardColour cardColour : CardColour.values()) {
                cardColour.loadImage();
            }
            for (MarbleColour marbleColour : MarbleColour.values()) {
                marbleColour.loadImage();
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void updatePlayerList(ArrayList<String> nicknames, String hostNickname) {
        guiWindow = guiWindow.refreshPlayerList(client, frame, buffer, nicknames, hostNickname);
    }

    @Override
    public void startGame(String line) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("command", "startGame");

        client.write(jsonObject.toString());
    }

    @Override
    public void startConnection() {
        ((Welcome) guiWindow).startConnection();
    }

    @Override
    public void connectionFailed(int timerDelay) {
        if (timerDelay > 0) {
            // TODO: handle reconnection
        } else {
            ((Welcome) guiWindow).connectionFailed();
        }
    }

    @Override
    public void terminate() {

    }

    @Override
    public void join() {

    }

    @Override
    public void loadGameInterface() {
        guiWindow.setActive(false, frame);
        guiWindow = new BoardView(client, buffer);
        guiWindow.setActive(true, frame);
    }
}
