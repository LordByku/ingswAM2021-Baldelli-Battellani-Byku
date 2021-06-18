package it.polimi.ingsw.view.gui.board.DevCardsArea;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;

import javax.swing.*;

public class GUIDevCardsArea implements LocalModelElementObserver {
    private final GUI gui;
    private JPanel devCardsArea;
    private Client client;
    private GUIDevCardsSlots guiDevCardsSlots;

    public GUIDevCardsArea(GUI gui, Client client, JPanel devCardsArea) {
        this.gui = gui;
        this.devCardsArea = devCardsArea;
        this.client = client;
    }

    public void loadDevCardsArea() {
        GUIDefaultProductionPower guiDefaultProductionPower = new GUIDefaultProductionPower(devCardsArea);
        guiDefaultProductionPower.loadDefaultProductionPower();

        loadSlots();
    }

    private void loadSlots() {
        guiDevCardsSlots = new GUIDevCardsSlots(gui, client, devCardsArea);
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
    }
}
