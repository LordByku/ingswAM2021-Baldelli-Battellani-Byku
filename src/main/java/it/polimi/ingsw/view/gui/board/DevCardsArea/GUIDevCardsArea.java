package it.polimi.ingsw.view.gui.board.DevCardsArea;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;

public class GUIDevCardsArea {
    private final GUI gui;
    private JPanel devCardsArea;
    private Client client;

    public GUIDevCardsArea(GUI gui, Client client, JPanel devCardsArea) {
        this.gui = gui;
        this.devCardsArea = devCardsArea;
        this.client = client;
    }

    public void loadDevCardsArea() {
        GUIDefaultProductionPower guiDefaultProductionPower = new GUIDefaultProductionPower(devCardsArea);
        guiDefaultProductionPower.loadDefaultProductionPower();

        GUIDevCardsSlots guiDevCardsSlots = new GUIDevCardsSlots(gui, client, devCardsArea);
        guiDevCardsSlots.loadDevCardsSlots();
    }
}
