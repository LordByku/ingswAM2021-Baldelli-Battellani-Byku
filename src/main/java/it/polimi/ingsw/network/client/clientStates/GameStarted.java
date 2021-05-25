package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.windows.CLIWindow;
import it.polimi.ingsw.view.cli.windows.viewWindows.ViewModel;

public class GameStarted extends ClientState {
    private boolean pendingUpdate;
    private final ViewInterface viewInterface;

    public GameStarted(ViewInterface viewInterface) {
        pendingUpdate = false;
        this.viewInterface = viewInterface;
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = JsonUtil.getInstance().parseLine(line).getAsJsonObject();
        String status = json.get("status").getAsString();

        switch (status) {
            case "error": {
                String message = json.get("message").getAsString();
                viewInterface.onError(client, message);
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

                        viewInterface.onCommand(client, player, commandBuffer);
                        break;
                    }
                    case "update": {
                        JsonObject message = json.getAsJsonObject("message");
                        client.getModel().updateModel(message);

                        viewInterface.onUpdate(client);
                        break;
                    }
                    default: {
                        viewInterface.onUnexpected(client);
                    }
                }
                break;
            }
            default: {
                viewInterface.onUnexpected(client);
            }
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        viewInterface.onUserInput(client, line);
    }
}
