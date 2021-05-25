package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;

public abstract class ClientState {
    public abstract void handleServerMessage(Client client, String line);

    public abstract void handleUserMessage(Client client, String line);
}
