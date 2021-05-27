package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.forms.Welcome;

public class InitWindow extends GUIWindow {
    public InitWindow(Client client) {
        super(new Welcome(client).getPanel());
    }
}
