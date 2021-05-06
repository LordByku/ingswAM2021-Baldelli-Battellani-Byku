package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;

public class InitResources extends ClientState {
    private final int initialResources;

    public InitResources(int initialResources) {
        this.initialResources = initialResources;
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {

    }
}
