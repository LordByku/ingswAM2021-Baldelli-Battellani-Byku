package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.forms.Lobby;

import java.util.ArrayList;

public class LobbyWindow extends GUIWindow {
    public LobbyWindow(Client client, ArrayList<String> nicknames, String hostNickname) {
        super(new Lobby(client, nicknames, hostNickname).getPanel());
    }
}
