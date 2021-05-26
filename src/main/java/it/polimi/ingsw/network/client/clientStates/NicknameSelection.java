package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

public class NicknameSelection extends ClientState {
    public NicknameSelection() {
        CLI.selectNickname();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        CLI.unexpected();
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if (!line.equals("")) {
            String nickname = line;
            client.setNickname(nickname);
            client.setState(new ModeSelection());
        } else {
            CLI.selectNickname();
        }
    }
}
