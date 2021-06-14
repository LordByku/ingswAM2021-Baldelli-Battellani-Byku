package it.polimi.ingsw.view.gui.windows.tokens;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.windows.GUIWindow;
import it.polimi.ingsw.view.gui.windows.MarbleMarketView;

public class MarbleMarketToken extends WindowToken {
    @Override
    public GUIWindow getWindow(GUI gui, Client client) {
        return new MarbleMarketView(gui, client);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && getClass() == o.getClass();
    }
}
