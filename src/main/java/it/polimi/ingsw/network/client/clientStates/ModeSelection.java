package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;

public class ModeSelection extends ClientState {
    public ModeSelection() {
        CLI.getInstance().selectMode();
    }

    @Override
    public void handleServerMessage(Client client, String line) {}

    @Override
    public void handleUserMessage(Client client, String line) {
        try {
            int mode = Integer.parseInt(line);

            switch(mode) {
                case 1: {
                    try {
                        client.openServerCommunication();
                        client.write(client.getNickname());
                        client.setState(new Lobby());
                    } catch (IOException e) {
                        CLI.getInstance().connectionError();
                        client.setState(new ModeSelection());
                    }
                    break;
                }
                case 2: {
                    client.setState(new LoadSinglePlayer());
                    break;
                }
                default: {
                    client.setState(new ModeSelection());
                }
            }
        } catch (NumberFormatException e) {
            client.setState(new ModeSelection());
        }
    }
}
