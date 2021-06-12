package it.polimi.ingsw.view.gui.board.DevCardsArea;

import it.polimi.ingsw.network.client.Client;

import javax.swing.*;

public class GUIDevCardsArea {

    JPanel devCardsArea;
    Client client;

    public GUIDevCardsArea(Client client, JPanel devCardsArea) {
        this.devCardsArea = devCardsArea;
        this.client = client;
    }

    public void loadDevCardsArea() {
        GUIDefaultProductionPower guiDefaultProductionPower = new GUIDefaultProductionPower(devCardsArea);
        guiDefaultProductionPower.loadDefaultProductionPower();

        GUIDevCardsSlots guiDevCardsSlots = new GUIDevCardsSlots(client, devCardsArea);
        guiDevCardsSlots.loadDevCardsSlots();
    }
}
