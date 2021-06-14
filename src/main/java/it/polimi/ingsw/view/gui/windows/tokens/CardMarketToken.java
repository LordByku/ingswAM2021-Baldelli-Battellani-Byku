package it.polimi.ingsw.view.gui.windows.tokens;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.windows.CardMarketView;
import it.polimi.ingsw.view.gui.windows.GUIWindow;

public class CardMarketToken extends WindowToken {
    @Override
    public GUIWindow getWindow(GUI gui, Client client) {
        return new CardMarketView(gui, client);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && getClass() == o.getClass();
    }
}
