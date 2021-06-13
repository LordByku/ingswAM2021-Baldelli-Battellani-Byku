package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.board.GUIMarbleMarket;

import javax.swing.*;

public class MarbleMarketView {
    private JPanel panel;
    private JPanel marketPanel;
    private JPanel bottomPanel;

    public MarbleMarketView(Client client){
        GUIMarbleMarket guiMarbleMarket = new GUIMarbleMarket(client, marketPanel);
        guiMarbleMarket.loadMarbleMarket();
    }
}

