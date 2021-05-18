package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.network.server.ServerParser;

import java.util.function.Consumer;

public class ProductionSelection extends ServerState{

    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {

        JsonObject json = ServerParser.getInstance().parseLine(line).getAsJsonObject();

        String command = ServerParser.getInstance().getCommand(json);
/*
        if(command.equals("production")) {
            int[] productionsSelected =  ServerParser.getInstance().parseIntArray(json.getAsJsonArray("activeSet"));

            if(Controller.getInstance().Production(clientHandler.getPerson(), productionsSelected)){
                Consumer<GameStateSerializer> lambda;
                synchronized (productionsSelected)
            }
        } else {
            clientHandler.error("Invalid command");
        }*/
    }
}
