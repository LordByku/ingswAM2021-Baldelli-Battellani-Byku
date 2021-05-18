package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.view.cli.CLI;

public abstract class SpendResources extends ClientState{
    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status){
            case "error":{
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                CLI.getInstance().purchaseDevCard();
                break;
            }
            case "ok":{
                String type = ClientParser.getInstance().getType(json);
                switch (type) {
                    case "confirm": {
                        CLI.getInstance().spendResourcesSuccess();
                        break;
                    }
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();
                        client.getModel().updateModel(message);
                        client.setState(new EndTurn());
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

    public void handleSelection(Client client, ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox){
        JsonObject jsonObject = new JsonObject();
        JsonObject spentResources = new JsonObject();
        spentResources.add("warehouse", ClientParser.getInstance().serialize(warehouse));
        spentResources.add("strongbox", ClientParser.getInstance().serialize(strongbox));
        jsonObject.addProperty("command", "spentResources");
        jsonObject.add("spentResources", spentResources);
        client.write(jsonObject.toString());

    }

    @Override
    public abstract void handleUserMessage(Client client, String line);
}
