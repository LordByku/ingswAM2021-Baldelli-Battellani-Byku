package it.polimi.ingsw.view.gui.windows.tokens;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.windows.EndGameView;
import it.polimi.ingsw.view.gui.windows.GUIWindow;

public class EndGameToken extends WindowToken{
    @Override
    public GUIWindow getWindow(GUI gui, Client client) {
        return  new EndGameView(gui, client);
    }
}
