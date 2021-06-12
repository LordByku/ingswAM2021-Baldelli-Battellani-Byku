package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public abstract class GUIWindow {
    public void setActive(boolean active, JFrame frame) {
        if (active) {
            frame.setContentPane(getPanel());
        }
        frame.setVisible(true);
    }

    protected abstract JPanel getPanel();

    public GUIWindow refreshPlayerList(Client client, JFrame frame, BlockingQueue<String> buffer, ArrayList<String> nicknames, String hostNickname) {
        setActive(false, frame);
        GUIWindow guiWindow = new Lobby(client, buffer, nicknames, hostNickname);
        guiWindow.setActive(true, frame);
        return guiWindow;
    }

    public abstract void onError(String message);
}

