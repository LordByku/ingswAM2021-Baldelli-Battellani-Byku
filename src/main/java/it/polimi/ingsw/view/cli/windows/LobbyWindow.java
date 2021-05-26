package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.cli.CLI;

public class LobbyWindow extends CLIWindow {
    @Override
    public void handleUserMessage(Client client, String line) {
        if (LocalConfig.getInstance().isHost()) {
            if (line.equals("")) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("command", "startGame");

                client.write(jsonObject.toString());
                return;
            }
        }

        render(client);
    }

    @Override
    public void render(Client client) {
        if (LocalConfig.getInstance().isHost()) {
            CLI.host();
        } else {
            CLI.waitStart();
        }
    }
}
