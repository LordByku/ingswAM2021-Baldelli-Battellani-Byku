package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.board.GUIBottomPanel;
import it.polimi.ingsw.view.gui.board.GUICardMarket;
import it.polimi.ingsw.view.gui.windows.tokens.CardMarketToken;

import javax.swing.*;

public class CardMarketView extends GUIWindow {
    private final GUICardMarket guiCardMarket;
    private JPanel bottomPanel;
    private JPanel marketPanel;
    private JPanel panel;

    public CardMarketView(GUI gui, Client client) {
        super(gui, client);
        guiCardMarket = new GUICardMarket(gui, client, marketPanel);
        guiCardMarket.loadCardMarket();

        GUIBottomPanel guiBottomPanel = new GUIBottomPanel(gui, client, bottomPanel, new CardMarketToken());
        guiBottomPanel.loadBottomPanel();
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    protected void clean() {
        guiCardMarket.clean();
    }

    @Override
    public void onError(String message) {

    }
}
