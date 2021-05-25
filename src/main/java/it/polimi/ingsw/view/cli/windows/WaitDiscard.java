package it.polimi.ingsw.view.cli.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

public class WaitDiscard extends CLIWindow {
    public WaitDiscard(Client client) {
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        CLI.getInstance().renderWindow(client);
    }

    @Override
    public void render(Client client) {
        CLI.getInstance().waitInitDiscard();
    }

    @Override
    public boolean refreshOnUpdate(Client client) {
        return client.getModel().allInitDiscard();
    }
}
