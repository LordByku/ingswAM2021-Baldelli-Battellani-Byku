package it.polimi.ingsw.view.cli.windows.viewWindows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.windows.CLIWindow;

public abstract class CLIViewWindow extends CLIWindow {
    @Override
    public boolean refreshOnUpdate(Client client) {
        return false;
    }
}
