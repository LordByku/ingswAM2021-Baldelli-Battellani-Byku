package it.polimi.ingsw.view.gui.board.DevCardsArea;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;

import javax.swing.*;
import java.awt.*;

public class GUIDevCardsArea implements LocalModelElementObserver {
    private final GUI gui;
    private final JPanel devCardsArea;
    private final Client client;
    private final String nickname;
    private GUIDevCardsSlots guiDevCardsSlots;
    private GUIDefaultProductionPower guiDefaultProductionPower;

    public GUIDevCardsArea(GUI gui, Client client, JPanel devCardsArea, String nickname) {
        this.gui = gui;
        this.devCardsArea = devCardsArea;
        this.client = client;
        this.nickname = nickname;

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {1, 1};
        devCardsArea.setLayout(gbl);
    }

    public void loadDevCardsArea() {
        guiDefaultProductionPower = new GUIDefaultProductionPower(gui, client, devCardsArea, nickname);
        guiDefaultProductionPower.loadDefaultProductionPower();

        loadSlots();
    }

    private void loadSlots() {
        guiDevCardsSlots = new GUIDevCardsSlots(gui, client, devCardsArea, nickname);
        guiDevCardsSlots.loadDevCardsSlots();
    }

    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        SwingUtilities.invokeLater(() -> {
            synchronized (client.getModel()) {
                devCardsArea.removeAll();
                loadSlots();
                devCardsArea.revalidate();
                devCardsArea.repaint();
            }
        });
    }

    @Override
    public void clean() {
        guiDevCardsSlots.clean();
        guiDefaultProductionPower.clean();
    }
}
