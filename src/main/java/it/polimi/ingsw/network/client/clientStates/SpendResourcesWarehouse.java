package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerSpendResourcesStrongbox;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerSpendResourcesWarehouse;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;


public class SpendResourcesWarehouse extends SpendResources{
    ConcreteResourceSet[] warehouse;
    ConcreteResourceSet strongbox;
    ConcreteResourceSet toSpend;
    int deckIndex;

    public SpendResourcesWarehouse(int numOfDepots, ConcreteResourceSet toSpend, int deckIndex){
        CLI.getInstance().toSpend(toSpend);
        CLI.getInstance().spendResourcesWarehouse();
        warehouse = new ConcreteResourceSet[numOfDepots];
        for(int i=0; i<numOfDepots; ++i){
            warehouse[i]= new ConcreteResourceSet();
        }
        strongbox = new ConcreteResourceSet();
        this.toSpend = toSpend;
        this.deckIndex=deckIndex;
    }

    public static ClientState builder(int numOfDepots, ConcreteResourceSet toSpend, int deckIndex) {
        if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
            return new SinglePlayerSpendResourcesWarehouse(numOfDepots, toSpend, deckIndex);
        } else {
            return new SpendResourcesWarehouse(numOfDepots, toSpend, deckIndex);
        }
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
            if(line.equalsIgnoreCase("strongbox")) {
                client.setState(SpendResourcesStrongbox.builder(warehouse, strongbox, toSpend, deckIndex));
            }
        }
        else if(arr.length == 2){
            try {
                int depotIndex = Integer.parseInt(arr[0]);
                int numOfResources = Integer.parseInt(arr[1]);
                if (depotIndex >= 0 && depotIndex < maxDepotSize && numOfResources > 0 && numOfResources <= depotSizes.get(depotIndex)) {
                    ConcreteResource resource = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().get(depotIndex).getResourceType();
                    ConcreteResourceSet set = new ConcreteResourceSet();
                    set.addResource(resource, numOfResources);
                    if (toSpend.contains(set)) {
                        warehouse[depotIndex].addResource(resource, numOfResources);
                        toSpend.removeResource(resource, numOfResources);
                        if(toSpend.size()==0) {
                            handleSelection(client, warehouse, strongbox);
                        }
                        else {
                            CLI.getInstance().toSpend(toSpend);
                            CLI.getInstance().spendResourcesWarehouse();
                        }
                    }
                }
            }
            catch (NumberFormatException e) {
                CLI.getInstance().purchaseDevCard();
            }
        }
    }
}
