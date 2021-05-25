package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.network.client.Client;

public interface ViewInterface {
    void onError(Client client, String message);

    void onCommand(Client client, String player, CommandBuffer commandBuffer);

    void onUpdate(Client client);

    void onUserInput(Client client, String line);

    void onUnexpected(Client client);
}
