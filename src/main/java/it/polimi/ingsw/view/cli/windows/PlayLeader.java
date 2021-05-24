package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.cli.CLI;

public class PlayLeader extends CommandWindow {
    public PlayLeader(Client client) {
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        try {
            int index = Integer.parseInt(line);
            JsonObject message = buildCommandMessage("index", JsonUtil.getInstance().serialize(index));
            client.write(message.toString());
            return;
        } catch (NumberFormatException e) {}

        render(client);
    }

    @Override
    public void render(Client client) {
        CLI.getInstance().playLeaderCard();
    }
}
