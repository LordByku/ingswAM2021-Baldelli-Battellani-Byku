package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerSpendResourcesWarehouse;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.view.cli.CLI;
import java.util.ArrayList;
import java.util.Map;

public class SpendResourcesProductionWarehouse extends SpendResourcesProduction{

    ConcreteResourceSet[] warehouse;
    ConcreteResourceSet strongbox;
    SpendableResourceSet toSpend;

    public SpendResourcesProductionWarehouse(int numOfDepots, SpendableResourceSet toSpend){
     //   CLI.getInstance().toSpend(toSpend);
        CLI.getInstance().spendResourcesWarehouse();
        warehouse = new ConcreteResourceSet[numOfDepots];
        for(int i=0; i<numOfDepots; ++i){
            warehouse[i]= new ConcreteResourceSet();
        }
        strongbox = new ConcreteResourceSet();
        this.toSpend = toSpend;
    }

    public static ClientState builder(int numOfDepots, SpendableResourceSet toSpend) {
        if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
            //return new SinglePlayerSpendResourcesWarehouse(numOfDepots, toSpend);
            //Start turn casuale
            return new StartTurn();
        } else {
            return new SpendResourcesProductionWarehouse(numOfDepots, toSpend);
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
            if(line.toLowerCase().equals("strongbox"))
                client.setState(new StartTurn());
                client.setState(SpendResourcesProductionStrongBox.builder(warehouse, strongbox, toSpend));

        }
        else if(arr.length == 2){
            try {
                int depotIndex = Integer.parseInt(arr[0]);
                int numOfResources = Integer.parseInt(arr[1]);
                System.out.println(depotIndex +" "+ numOfResources);
                System.out.println("maxDepotsize: "+maxDepotSize);


                if (depotIndex >= 0 && depotIndex < maxDepotSize && numOfResources > 0 && numOfResources <= depotSizes.get(depotIndex)) {
                    ConcreteResource resource = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().get(depotIndex).getResourceType();
                   // SpendableResourceSet set = new SpendableResourceSet();
                    ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
                    concreteResourceSet.addResource(resource, numOfResources);

                    if (toSpend.getResourceSet().getConcreteResources().contains(concreteResourceSet)) {
                        warehouse[depotIndex].addResource(resource, numOfResources);
                        toSpend.getResourceSet().getConcreteResources().removeResource(resource, numOfResources);
                        if(toSpend.size()==0) {
                            handleSelection(client, warehouse, strongbox);
                        }
                        else {
                       //     CLI.getInstance().toSpend(toSpend);
                            CLI.getInstance().spendResourcesWarehouse();
                        }
                    }
                    else if (toSpend.getResourceSet().getChoiceResources().size()>=concreteResourceSet.size()) {
                        warehouse[depotIndex].addResource(resource, numOfResources);
                        for(int i =0; i< numOfResources; i++)
                            toSpend.getResourceSet().getChoiceResources().remove(resource);

                        if (toSpend.size() == 0) {
                            handleSelection(client, warehouse, strongbox);
                        } else {
                            //     CLI.getInstance().toSpend(toSpend);
                            CLI.getInstance().spendResourcesWarehouse();
                        }
                    }
                }
            }
            catch (NumberFormatException e) {
               // CLI.getInstance().purchaseDevCard();
            }
        }
    }
}
