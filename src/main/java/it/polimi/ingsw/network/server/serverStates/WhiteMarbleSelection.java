package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.ServerParser;

public class WhiteMarbleSelection extends ServerState {
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line).getAsJsonObject();
        String command = ServerParser.getInstance().getCommand(json);

        switch (command) {
            case "whiteMarble": {
                JsonArray resources = ServerParser.getInstance().getResources(json);
                ConcreteResource[] choices = ServerParser.getInstance().parseConcreteResourceArray(resources);

                if(Controller.getInstance().whiteMarble(choices)) {
                    JsonObject outMessage = new JsonObject();
                    outMessage.add("obtained", ServerParser.getInstance().serialize(Controller.getInstance().concreteToObtain()));
                    clientHandler.ok("confirm", outMessage);
                    clientHandler.setState(new ManageWarehouse());
                } else {
                    clientHandler.error("Invalid Choice");
                }

                break;
            }
            default: {
                clientHandler.error("Invalid Command");
            }
        }
    }
}
