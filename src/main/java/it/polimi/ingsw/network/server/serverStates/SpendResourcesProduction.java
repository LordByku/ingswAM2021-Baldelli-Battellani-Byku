package it.polimi.ingsw.network.server.serverStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.network.server.ServerParser;
import it.polimi.ingsw.network.server.serverStates.EndTurn;
import it.polimi.ingsw.network.server.serverStates.ServerState;

import java.util.function.Consumer;

public class SpendResourcesProduction extends ServerState {

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

                if(Controller.getInstance().Production(clientHandler.getPerson(),warehouse,strongbox)){
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("status","ok");
                    jsonObject.addProperty("type","confirm");
                    clientHandler.confirm();
                    Consumer<GameStateSerializer> lambda = (serializer) -> {
                        serializer.addFaithTrack(clientHandler.getPerson());
                        serializer.addWarehouse(clientHandler.getPerson());
                        serializer.addStrongbox(clientHandler.getPerson());
                        serializer.addCardMarket();
                    };
                    clientHandler.updateGameState(lambda);
                    clientHandler.setState(new ChoiceResources());
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
