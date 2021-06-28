package it.polimi.ingsw.view.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.gameZone.marbles.MarbleColour;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.GUIClientUserCommunication;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;
import it.polimi.ingsw.view.gui.windows.BoardView;
import it.polimi.ingsw.view.gui.windows.EndGameView;
import it.polimi.ingsw.view.gui.windows.GUIWindow;
import it.polimi.ingsw.view.gui.windows.Welcome;
import it.polimi.ingsw.view.gui.windows.tokens.WindowToken;

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

    public void bufferWrite(String message) {
        try {
            buffer.put(message);
        } catch (InterruptedException e) {
        }
    }

    public void switchGameWindow(WindowToken token) {
        SwingUtilities.invokeLater(() -> {
            guiWindow.setActive(false, frame);
            guiWindow = token.getWindow(this, client);
            guiWindow.setActive(true, frame);
        });
    }

    @Override
    public void onFatalError(String message) {
        SwingUtilities.invokeLater(() -> {
            guiWindow.onError(message);
        });
    }

    @Override
    public void onError(String message) {
        SwingUtilities.invokeLater(() -> {
            guiWindow.onError(message);
        });
    }

    @Override
    public void onCommand(String player, CommandBuffer commandBuffer) {
        guiWindow.clearErrors();
    }

    @Override
    public void onUpdate() {
        guiWindow.clearErrors();
    }

    @Override
    public void onUserInput(String line) {
        System.out.println("sending: " + line);
        client.write(line);
    }

    @Override
    public void onUnexpected() {

    }

    @Override
    public void onEndGame(JsonObject endGameMessage) {
        client.getModel().setEndGameResults(endGameMessage);
        SwingUtilities.invokeLater(() -> {
            guiWindow.setActive(false, frame);
            guiWindow = new EndGameView(this, client);
            guiWindow.setActive(true, frame);
        });
    }

    @Override
    public void init() {
        SwingUtilities.invokeLater(() -> {
            guiWindow = new Welcome(this, client);
            guiWindow.setActive(true, frame);
        });
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
        SwingUtilities.invokeLater(() -> {
            guiWindow = guiWindow.refreshPlayerList(client, frame, buffer, nicknames, hostNickname);
        });
    }

    @Override
    public void startGame(String line) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("command", "startGame");
        if (!line.equals("")) {
            JsonObject config = JsonUtil.getInstance().parseLine(line).getAsJsonObject();
            jsonObject.add("config", config);
        }
        System.out.println(jsonObject.toString());

        client.write(jsonObject.toString());
    }

    @Override
    public void startConnection() {
        SwingUtilities.invokeLater(() -> {
            ((Welcome) guiWindow).startConnection();
        });
    }

    @Override
    public void connectionFailed(int timerDelay) {
        SwingUtilities.invokeLater(() -> {
            guiWindow.connectionFailed(timerDelay);
        });
    }

    @Override
    public void terminate() {

    }

    @Override
    public void join() {

    }

    @Override
    public void loadGameInterface() {
        SwingUtilities.invokeLater(() -> {
            guiWindow.setActive(false, frame);
            guiWindow = new BoardView(this, client, client.getNickname());
            guiWindow.setActive(true, frame);
        });
    }
}
