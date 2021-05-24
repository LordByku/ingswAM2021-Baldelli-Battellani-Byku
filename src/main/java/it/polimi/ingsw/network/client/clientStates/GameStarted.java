package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.windows.viewWindows.CLIViewWindow;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.windows.CLIWindow;

public class GameStarted extends ClientState {
    private CLIWindow cliWindow;
    private CLIViewWindow cliViewWindow;

    public GameStarted(Client client) {
        cliWindow = null;
        cliViewWindow = null;
    }

    public CLIWindow getActiveWindow() {
        if(cliViewWindow != null) {
            return cliViewWindow;
        }
        return cliWindow;
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = JsonUtil.getInstance().parseLine(line).getAsJsonObject();
        String status = json.get("status").getAsString();

        switch (status) {
            case "error": {
                String message = json.get("message").getAsString();
                CLI.getInstance().serverError(message);
                cliWindow = CLIWindow.refresh(client);
                cliWindow.render(client);
                break;
            }
            case "ok": {
                String type = json.get("type").getAsString();

                switch (type) {
                    case "command": {
                        JsonObject message = json.getAsJsonObject("message");
                        String player = message.get("player").getAsString();
                        CommandBuffer commandBuffer = Deserializer.getInstance().getCommandBuffer(message.get("value"));

                        System.out.println("Command buffer from " + player + " : " + message.get("value"));

                        client.getModel().getPlayer(player).setCommandBuffer(commandBuffer);

                        if(player.equals(client.getNickname())) {
                            cliWindow = CLIWindow.refresh(client);
                            cliWindow.render(client);
                        }
                        break;
                    }
                    case "update": {
                        JsonObject message = json.getAsJsonObject("message");
                        client.getModel().updateModel(message);
                        if(cliWindow == null || cliWindow.refreshOnUpdate(client)) {
                            cliWindow = CLIWindow.refresh(client);
                            cliWindow.render(client);
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
        getActiveWindow().handleUserMessage(client, line);
    }
}
