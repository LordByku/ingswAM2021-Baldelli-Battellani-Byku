package it.polimi.ingsw.view.gui.windows.tokens;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.windows.GUIWindow;

public abstract class WindowToken {
    public abstract GUIWindow getWindow(GUI gui, Client client);
}
