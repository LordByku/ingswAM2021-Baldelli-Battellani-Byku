package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.ViewInterface;

public class GameStarted extends ClientState {
    private final ViewInterface viewInterface;

    public GameStarted(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = JsonUtil.getInstance().parseLine(line).getAsJsonObject();
        String status = json.get("status").getAsString();

        switch (status) {
            case "error": {
                String message = json.get("message").getAsString();
                viewInterface.onError(message);
                break;
            }
            case "ok": {
                String type = json.get("type").getAsString();

                switch (type) {
                    case "endGame": {
                        JsonObject message = json.getAsJsonObject("message");
                        viewInterface.onEndGame(message);
                        break;
                    }
                    case "command": {
                        JsonObject message = json.getAsJsonObject("message");
                        String player = message.get("player").getAsString();
                        CommandBuffer commandBuffer = Deserializer.getInstance().getCommandBuffer(message.get("value"));

                        System.out.println(message.get("value").toString());

                        client.getModel().getPlayer(player).setCommandBuffer(commandBuffer);

                        viewInterface.onCommand(player, commandBuffer);
                        break;
                    }
                    case "update": {
                        JsonObject message = json.getAsJsonObject("message");
                        client.getModel().updateModel(message);

                        viewInterface.onUpdate();
                        break;
                    }
                    default: {
                        viewInterface.onUnexpected();
                    }
                }
                break;
            }
            default: {
                viewInterface.onUnexpected();
            }
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        viewInterface.onUserInput(line);
    }
}
