package it.polimi.ingsw.view.gui.board.DevCardsArea;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;

import javax.swing.*;

public class GUIDevCardsArea implements LocalModelElementObserver {
    private final GUI gui;
    private JPanel devCardsArea;
    private Client client;
    private String nickname;
    private GUIDevCardsSlots guiDevCardsSlots;
    private GUIDefaultProductionPower guiDefaultProductionPower;

    public GUIDevCardsArea(GUI gui, Client client, JPanel devCardsArea, String nickname) {
        this.gui = gui;
        this.devCardsArea = devCardsArea;
        this.client = client;
        this.nickname = nickname;
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
    public void notifyObserver() {
        SwingUtilities.invokeLater(() -> {
            devCardsArea.removeAll();
            loadSlots();
            devCardsArea.revalidate();
            devCardsArea.repaint();
        });
    }

    @Override
    public void clean() {
        guiDevCardsSlots.clean();
        guiDefaultProductionPower.clean();
    }
}
