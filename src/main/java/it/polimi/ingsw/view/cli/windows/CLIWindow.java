package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.localModel.LocalModel;
import it.polimi.ingsw.view.localModel.Player;

public abstract class CLIWindow {
    public static CLIWindow refresh(Client client) {
        LocalModel model = client.getModel();
        Player self = model.getPlayer(client.getNickname());

        CommandBuffer buffer = self.getCommandBuffer();
        if (buffer != null && !buffer.isCompleted()) {
            return CommandWindow.build(client, buffer.getCommandType());
        }

        if (!model.allInitDiscard()) {
            if (!self.initDiscard()) {
                JsonObject message = client.buildRequestMessage(CommandType.INITDISCARD);
                client.write(message.toString());
                return null;
            }
            return new WaitDiscard(client);
        }

        if (!model.allInitResources()) {
            if (!self.initResources()) {
                JsonObject message = client.buildRequestMessage(CommandType.INITRESOURCES);
                client.write(message.toString());
                return null;
            }
            return new WaitResources(client);
        }

        return new CommandSelection(client);
    }

    public abstract void handleUserMessage(Client client, String line);

    public abstract void render(Client client);

    public boolean refreshOnUpdate(Client client) {
        return true;
    }
}
