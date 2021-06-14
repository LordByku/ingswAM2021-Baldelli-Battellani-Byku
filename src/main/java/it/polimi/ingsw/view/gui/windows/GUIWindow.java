package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public abstract class GUIWindow {
    protected final GUI gui;
    protected final Client client;

    protected GUIWindow(GUI gui, Client client) {
        this.gui = gui;
        this.client = client;
    }

    public void setActive(boolean active, JFrame frame) {
        if (active) {
            frame.setContentPane(getPanel());
        }
        frame.setVisible(true);
    }

    protected abstract JPanel getPanel();

    public GUIWindow refreshPlayerList(Client client, JFrame frame, BlockingQueue<String> buffer, ArrayList<String> nicknames, String hostNickname) {
        setActive(false, frame);
        GUIWindow guiWindow = new Lobby(gui, client, nicknames, hostNickname);
        guiWindow.setActive(true, frame);
        return guiWindow;
    }

    public abstract void onError(String message);
}

