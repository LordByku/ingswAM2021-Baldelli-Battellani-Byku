package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.Player;

public class WaitResources extends CLIWindow {
    public WaitResources(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        if(!self.initResources()) {
            JsonObject message = buildRequestMessage(CommandType.INITRESOURCES);
            client.write(message.toString());
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        render(client);
    }

    @Override
    public void render(Client client) {
        CLI.getInstance().waitInitResources();
    }

    @Override
    public boolean refreshOnUpdate(Client client) {
        return client.getModel().allInitResources();
    }
}
