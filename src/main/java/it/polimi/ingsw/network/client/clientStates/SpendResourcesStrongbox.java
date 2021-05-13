package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.view.cli.CLI;

public class SpendResourcesStrongbox extends ClientState{
    ConcreteResourceSet[] warehouse;
    ConcreteResourceSet strongbox;
    ConcreteResourceSet toSpend;

    public SpendResourcesStrongbox(ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox, ConcreteResourceSet toSpend){
        this.warehouse = warehouse;
        this.strongbox = strongbox;
        this.toSpend = toSpend;
    }

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
            }
            default: {
                CLI.getInstance().unexpected();
            }
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {

        String[] arr = line.split(" ");
        if(arr.length==1){
            if(line.equals("warehouse")){
                int numOfDepots = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().size();
                client.setState(new SpendResourcesWarehouse(numOfDepots, toSpend));
            }
        }
        else{
            try {
                int numOfResources = Integer.parseInt(arr[0]);
                ConcreteResource resource = client.readUserResource(arr[1]);
                if(resource!=null && numOfResources>0 && numOfResources <=client.getModel().getPlayer(client.getNickname()).getBoard().getStrongBox().getCount(resource)){
                    ConcreteResourceSet set = new ConcreteResourceSet();
                    set.addResource(resource, numOfResources);
                    if(toSpend.contains(set)){
                        strongbox.addResource(resource,numOfResources);
                        toSpend.removeResource(resource,numOfResources);
                        if(toSpend.size()==0) {
                            JsonObject jsonObject = new JsonObject();
                            JsonObject spentResources = new JsonObject();
                            spentResources.add("warehouse", ClientParser.getInstance().serialize(warehouse));
                            spentResources.add("strongbox", ClientParser.getInstance().serialize(strongbox));
                            jsonObject.addProperty("command", "spentResources");
                            jsonObject.add("spentResources", spentResources);
                            client.write(jsonObject.toString());
                        }
                    }
                }
            }
            catch (NumberFormatException | JsonSintaxException e) {
                CLI.getInstance().purchaseDevCard();
            }
        }
    }
}
