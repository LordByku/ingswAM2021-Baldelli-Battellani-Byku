package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.board.GUIBottomPanel;
import it.polimi.ingsw.view.gui.board.GUIMarbleMarket;
import it.polimi.ingsw.view.gui.windows.tokens.MarbleMarketToken;

import javax.swing.*;

public class MarbleMarketView extends GUIWindow {
    private JPanel panel;
    private JPanel marketPanel;
    private JPanel bottomPanel;

    public MarbleMarketView(GUI gui, Client client) {
        super(gui, client);
        GUIMarbleMarket guiMarbleMarket = new GUIMarbleMarket(client, marketPanel);
        guiMarbleMarket.loadMarbleMarket();

        GUIBottomPanel guiBottomPanel = new GUIBottomPanel(gui, client, bottomPanel, new MarbleMarketToken());
        guiBottomPanel.loadBottomPanel();
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    public void onError(String message) {

    }
}

