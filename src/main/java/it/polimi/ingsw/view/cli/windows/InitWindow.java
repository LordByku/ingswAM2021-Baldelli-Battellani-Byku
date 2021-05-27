package it.polimi.ingsw.view.cli.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

public class InitWindow extends CLIWindow {
    @Override
    public void handleUserMessage(Client client, String line) {
    }

    @Override
    public void render(Client client) {
        if(client.getNickname() == null) {
            CLI.selectNickname();
        } else {
            CLI.selectMode();
        }
    }
}
