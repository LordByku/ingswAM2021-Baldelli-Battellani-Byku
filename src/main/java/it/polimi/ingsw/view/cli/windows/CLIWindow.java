package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.localModel.LocalModel;
import it.polimi.ingsw.view.localModel.Player;

import java.util.ArrayList;

public abstract class CLIWindow {
    public abstract void handleUserMessage(Client client, String line);
    public abstract void render(Client client);

    public boolean refreshOnUpdate(Client client) {
        return true;
    }

    public static CLIWindow refresh(Client client) {
        System.out.println("refreshing CLI window");

        LocalModel model = client.getModel();
        Player self = model.getPlayer(client.getNickname());

        CommandBuffer buffer = self.getCommandBuffer();
        if(buffer != null && !buffer.isCompleted()) {
            return CommandWindow.build(client, buffer.getCommandType());
        }

        if(!model.allInitDiscard()) {
            return new WaitDiscard(client);
        }

        if(!model.allInitResources()) {
            return new WaitResources(client);
        }

        return new CommandSelection(client);
    }

    public JsonObject buildRequestMessage(CommandType commandType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("request", "newCommand");
        jsonObject.add("command", JsonUtil.getInstance().serialize(commandType));
        return jsonObject;
    }

    public JsonObject buildCommandMessage(String command, JsonElement value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("request", "action");
        jsonObject.addProperty("command", command);
        jsonObject.add("value", value);
        return jsonObject;
    }

    public JsonObject buildCancelMessage() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("request", "cancel");
        return jsonObject;
    }
}
