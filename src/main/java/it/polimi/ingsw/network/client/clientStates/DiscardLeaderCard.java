package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;

import java.util.function.Supplier;

public class DiscardLeaderCard extends ClientState {
    private final Supplier<ClientState> returnStateSupplier;

    public DiscardLeaderCard(Supplier<ClientState> returnStateSupplier) {
        this.returnStateSupplier = returnStateSupplier;
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {

    }
}
