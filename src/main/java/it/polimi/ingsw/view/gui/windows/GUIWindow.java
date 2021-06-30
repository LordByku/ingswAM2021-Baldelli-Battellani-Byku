package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public abstract class GUIWindow {
    protected final GUI gui;
    protected final Client client;
    private Popup reconnectionPopup = null;
    private ButtonClickEvent lock;

    protected GUIWindow(GUI gui, Client client) {
        this.gui = gui;
        this.client = client;
    }

    public void setActive(boolean active, JFrame frame) {
        if (active) {
            frame.setContentPane(getPanel());
        } else {
            clean();
        }
        frame.setVisible(true);
    }

    protected abstract JPanel getPanel();

    protected abstract void clean();

    public GUIWindow refreshPlayerList(Client client, JFrame frame, BlockingQueue<String> buffer, ArrayList<String> nicknames, String hostNickname) {
        setActive(false, frame);
        GUIWindow guiWindow = new Lobby(gui, client, nicknames, hostNickname);
        guiWindow.setActive(true, frame);
        return guiWindow;
    }

    public GUIWindow onFatalError(JFrame frame, String message) {
        setActive(false, frame);
        GUIWindow guiWindow = new Welcome(gui, client);
        guiWindow.setActive(true, frame);
        guiWindow.onError(message);
        return guiWindow;
    }

    public abstract void onError(String message);

    public abstract void connectionFailed(int timerDelay);

    public abstract void clearErrors();

    protected void showReconnectionPopup(int timerDelay) {
        JPanel popupPanel = new JPanel(new GridBagLayout());
        popupPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel label = GUIUtil.addLabel("Disconnected from server, reconnecting in " + (timerDelay / 1000) + " seconds...", popupPanel);
        Dimension panelSize = label.getPreferredSize();
        label.setForeground(Color.RED);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if(reconnectionPopup != null) {
            reconnectionPopup.hide();
        }
        reconnectionPopup = PopupFactory.getSharedInstance().getPopup(getPanel(), popupPanel,
                (screenSize.width - panelSize.width) / 2,
                (screenSize.height - panelSize.height) / 2);
        reconnectionPopup.show();

        lock = new ButtonClickEvent((e) -> {}, true);
    }

    protected void hideReconnectionPopup() {
        if(reconnectionPopup != null) {
            reconnectionPopup.hide();
            reconnectionPopup = null;
            lock.actionPerformed(null);
        }
    }

    public void startConnection() {
        if(reconnectionPopup != null) {
            reconnectionPopup.hide();
            JPanel popupPanel = new JPanel(new GridBagLayout());
            popupPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel label = GUIUtil.addLabel("Connecting to server...", popupPanel);
            Dimension panelSize = label.getSize();
            label.setForeground(Color.RED);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            reconnectionPopup = PopupFactory.getSharedInstance().getPopup(getPanel(), popupPanel,
                    (screenSize.width - panelSize.width) / 2,
                    (screenSize.height - panelSize.height) / 2);
            reconnectionPopup.show();
        }
    }
}

