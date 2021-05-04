package it.polimi.ingsw.network.clientStates;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.cli.CLI;

public class NicknameSelection extends ClientState {
    public NicknameSelection() {
        CLI.getInstance().selectNickname();
    }
    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String nickname = line;
        client.setNickname(nickname);
        client.setState(new ModeSelection());
    }
}
