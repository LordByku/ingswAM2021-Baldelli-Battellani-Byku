package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.windows.CLIWindow;
import it.polimi.ingsw.view.cli.windows.viewWindows.ViewModel;

public class GameStarted extends ClientState {
    private boolean pendingUpdate;

    public GameStarted() {
        pendingUpdate = false;
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = JsonUtil.getInstance().parseLine(line).getAsJsonObject();
        String status = json.get("status").getAsString();

        switch (status) {
            case "error": {
                String message = json.get("message").getAsString();
                CLI.getInstance().serverError(message);
                CLI.getInstance().refreshWindow(client);
                CLI.getInstance().renderWindow(client);
                break;
            }
            case "ok": {
                String type = json.get("type").getAsString();

                switch (type) {
                    case "command": {
                        JsonObject message = json.getAsJsonObject("message");
                        String player = message.get("player").getAsString();
                        CommandBuffer commandBuffer = Deserializer.getInstance().getCommandBuffer(message.get("value"));

                        client.getModel().getPlayer(player).setCommandBuffer(commandBuffer);

                        if (pendingUpdate || player.equals(client.getNickname())) {
                            pendingUpdate = false;
                            CLI.getInstance().refreshWindow(client);
                            CLI.getInstance().renderWindow(client);
                        }
                        break;
                    }
                    case "update": {
                        JsonObject message = json.getAsJsonObject("message");
                        client.getModel().updateModel(message);

                        CLIWindow currentWindow = CLI.getInstance().getActiveWindow();
                        if (currentWindow == null || currentWindow.refreshOnUpdate(client)) {
                            pendingUpdate = true;
                        }
                        break;
                    }
                    default: {
                        CLI.getInstance().unexpected();
                    }
                }
                break;
            }
            default: {
                CLI.getInstance().unexpected();
            }
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if (line.equals("v")) {
            if (CLI.getInstance().getViewWindow() == null) {
                CLI.getInstance().setViewWindow(new ViewModel());
            } else {
                CLI.getInstance().setViewWindow(null);
            }
            CLI.getInstance().renderWindow(client);
            return;
        }
        CLI.getInstance().getActiveWindow().handleUserMessage(client, line);
    }
}
