package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;

public class BoardComponentSelection extends ClientState {
    private final String nickname;

    public BoardComponentSelection(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {

    }
}
