package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.network.server.ServerParser;

import java.util.function.Consumer;

public class SpendResources extends ServerState{
    int deckIndex;

    public SpendResources(int deckIndex){
        this.deckIndex= deckIndex;
    }
    @Override
    public void handleClientMessage(ClientHandler clientHandler, String line) {
        JsonObject json = ServerParser.getInstance().parseLine(line).getAsJsonObject();
        String command = ServerParser.getInstance().getCommand(json);

        if(command.equals("spentResources")){
            JsonObject spentResources = ServerParser.getInstance().getSpentResources(json);
            JsonArray jsonArray = spentResources.get("warehouse").getAsJsonArray();
            int numOfDepots = clientHandler.getPerson().getBoard().getWarehouse().numberOfDepots();
            ConcreteResourceSet[] warehouse = new ConcreteResourceSet[numOfDepots];
            if(jsonArray.size()==numOfDepots) {
                int i=0;
                for (JsonElement jsonElement : jsonArray) {
                    warehouse[i] = ServerParser.getInstance().getConcreteResourceSet(jsonElement.getAsJsonObject());
                    i++;
                }
                ConcreteResourceSet strongbox = ServerParser.getInstance().getConcreteResourceSet(spentResources.get("strongbox").getAsJsonObject());

                if(Controller.getInstance().spendResources(clientHandler.getPerson(),deckIndex,warehouse,strongbox)){
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("status","ok");
                    jsonObject.addProperty("type","confirm");
                    clientHandler.confirm();
                    Consumer<GameStateSerializer> lambda = (serializer) -> {
                        serializer.addDevCards(clientHandler.getPerson());
                        serializer.addWarehouse(clientHandler.getPerson());
                        serializer.addStrongbox(clientHandler.getPerson());
                        serializer.addCardMarket();
                    };
                    clientHandler.updateGameState(lambda);
                    clientHandler.setState(new EndTurn());
                }
                else{
                    clientHandler.error("Incorrect resource choice");
                }
            }
            else{
                clientHandler.error("wrong warehouse depot size");
            }
        }else{
            clientHandler.error("Invalid command");
        }

    }
}
