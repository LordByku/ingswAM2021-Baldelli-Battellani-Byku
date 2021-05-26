package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.ViewInterface;

public class NicknameSelection extends ClientState {
    private final ViewInterface viewInterface;

    public NicknameSelection(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        viewInterface.onUnexpected(client);
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String nickname = line;
        client.setNickname(nickname);
        client.setState(new ModeSelection(viewInterface));
        viewInterface.selectMode(client);
    }
}
