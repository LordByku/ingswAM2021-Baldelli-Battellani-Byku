package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.cli.CLI;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

public class LobbyWindow extends CLIWindow {
    private JsonObject customConfig = null;
    private Boolean success = null;

    @Override
    public void handleUserMessage(Client client, String line) {
        if (LocalConfig.getInstance().isHost()) {
            if (line.equals("")) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("command", "startGame");
                if (customConfig != null) {
                    jsonObject.add("config", customConfig);
                }

                client.write(jsonObject.toString());
                return;
            } else {
                success = false;
                File file = new File(line);
                try {
                    InputStreamReader reader = new FileReader(file);
                    customConfig = new JsonParser().parse(reader).getAsJsonObject();
                    success = true;
                } catch (Exception e) {
                    success = false;
                }
            }
        }

        render(client);
    }

    @Override
    public void render(Client client) {
        if (LocalConfig.getInstance().isHost()) {
            if (success != null) {
                if (success) {
                    CLI.loadSuccessful();
                } else {
                    CLI.loadFailed();
                }
                success = null;
            }
            CLI.host();
        } else {
            CLI.waitStart();
        }
    }
}
