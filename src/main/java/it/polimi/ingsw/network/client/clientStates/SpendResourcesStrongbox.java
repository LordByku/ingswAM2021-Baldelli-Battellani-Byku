package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.view.cli.CLI;

public class SpendResourcesStrongbox extends SpendResources{
    ConcreteResourceSet[] warehouse;
    ConcreteResourceSet strongbox;
    ConcreteResourceSet toSpend;

    public SpendResourcesStrongbox(ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox, ConcreteResourceSet toSpend){
        this.warehouse = warehouse;
        this.strongbox = strongbox;
        this.toSpend = toSpend;
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        CLI.getInstance().toSpend(toSpend);
        CLI.getInstance().spendResourcesStrongbox();
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
                ConcreteResource resource = ClientParser.getInstance().readUserResource(arr[1]);
                if(resource!=null && numOfResources>0 && numOfResources <=client.getModel().getPlayer(client.getNickname()).getBoard().getStrongBox().getCount(resource)){
                    ConcreteResourceSet set = new ConcreteResourceSet();
                    set.addResource(resource, numOfResources);
                    if(toSpend.contains(set)){
                        strongbox.addResource(resource,numOfResources);
                        toSpend.removeResource(resource,numOfResources);
                        if(toSpend.size()==0) {
                            write(client, warehouse, strongbox);
                        }
                    }
                }
            }
            catch (NumberFormatException | JsonSyntaxException e) {
                CLI.getInstance().purchaseDevCard();
            }
        }
    }
}
