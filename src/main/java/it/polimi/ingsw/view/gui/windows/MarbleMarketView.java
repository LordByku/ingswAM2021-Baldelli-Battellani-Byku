package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.board.GUIMarbleMarket;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;

public class MarbleMarketView extends GUIWindow {
    private JPanel panel;
    private JPanel marketPanel;
    private JPanel bottomPanel;

    public MarbleMarketView(Client client, BlockingQueue<String> buffer) {
        GUIMarbleMarket guiMarbleMarket = new GUIMarbleMarket(client, marketPanel);
        guiMarbleMarket.loadMarbleMarket();
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    public void onError(String message) {

    }
}

