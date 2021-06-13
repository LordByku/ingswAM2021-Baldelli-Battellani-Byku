package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.board.GUICardMarket;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;

public class CardMarketView extends GUIWindow {
    private JPanel bottomPanel;
    private JPanel marketPanel;
    private JPanel panel;

    public CardMarketView(Client client, BlockingQueue<String> buffer) {
        GUICardMarket cardMarket = new GUICardMarket(marketPanel, client, buffer);
        cardMarket.loadCardMarket();

        // TODO : add bottom panel
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    public void onError(String message) {

    }
}
