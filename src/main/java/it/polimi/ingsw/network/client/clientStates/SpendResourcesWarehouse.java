package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;


public class SpendResourcesWarehouse extends ClientState{
    ConcreteResourceSet[] warehouse;
    ConcreteResourceSet strongbox;
    ConcreteResourceSet toSpend;

    public SpendResourcesWarehouse(int numOfDepots, ConcreteResourceSet toSpend){
        warehouse = new ConcreteResourceSet[numOfDepots];
        strongbox = new ConcreteResourceSet();
        this.toSpend = toSpend;
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {
        int maxDepotSize = 0;
        ArrayList<Integer> depotSizes = BoardParser.getInstance().getDepotSizes();
        for(Integer size: depotSizes)
            if(size>maxDepotSize)
                maxDepotSize=size;

        String[] arr = line.split(" ");

        if(arr.length==1){
            if(line.toLowerCase().equals("strongbox"))
                client.setState(new SpendResourcesStrongbox(warehouse, strongbox, toSpend));
        }
        else{
            try {
                int depotIndex = Integer.parseInt(arr[0]);
                int numOfResources = Integer.parseInt(arr[1]);
                if (depotIndex >= 0 && depotIndex < maxDepotSize && numOfResources > 0 && numOfResources < depotSizes.get(depotIndex)) {
                    ConcreteResource resource = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().get(depotIndex).getResourceType();
                    ConcreteResourceSet set = new ConcreteResourceSet();
                    set.addResource(resource, numOfResources);
                    if (toSpend.contains(set)) {
                        warehouse[depotIndex].addResource(resource, numOfResources);
                        toSpend.removeResource(resource, numOfResources);
                    }
                }
            }
            catch (NumberFormatException e) {
                CLI.getInstance().purchaseDevCard();
            }
        }
    }
}
