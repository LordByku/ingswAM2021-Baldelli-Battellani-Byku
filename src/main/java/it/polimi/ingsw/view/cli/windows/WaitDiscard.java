package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.Player;

public class WaitDiscard extends CLIWindow {
    public WaitDiscard(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        if(!self.initDiscard()) {
            JsonObject message = buildRequestMessage(CommandType.INITDISCARD);
            client.write(message.toString());
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        render(client);
    }

    @Override
    public void render(Client client) {
        CLI.getInstance().waitInitDiscard();
    }

    @Override
    public boolean refreshOnUpdate(Client client) {
        return client.getModel().allInitDiscard();
    }
}
