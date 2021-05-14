package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.parsing.BoardParser;
import it.polimi.ingsw.parsing.Parser;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;


public class SpendResourcesWarehouse extends SpendResources{
    ConcreteResourceSet[] warehouse;
    ConcreteResourceSet strongbox;
    ConcreteResourceSet toSpend;

    public SpendResourcesWarehouse(int numOfDepots, ConcreteResourceSet toSpend){
        CLI.getInstance().spendResourcesWarehouse();
        warehouse = new ConcreteResourceSet[numOfDepots];
        for(int i=0; i<numOfDepots; ++i){
            warehouse[i]= new ConcreteResourceSet();
        }
        strongbox = new ConcreteResourceSet();
        this.toSpend = toSpend;
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
        else if(arr.length == 2){
            try {
                int depotIndex = Integer.parseInt(arr[0]);
                int numOfResources = Integer.parseInt(arr[1]);
                System.out.println(depotIndex +" "+ numOfResources);
                System.out.println("maxDepotsize: "+maxDepotSize);
                if (depotIndex >= 0 && depotIndex < maxDepotSize && numOfResources > 0 && numOfResources <= depotSizes.get(depotIndex)) {
                    ConcreteResource resource = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().get(depotIndex).getResourceType();
                    System.out.println("resource type: " +resource);
                    System.out.println("to spend: " + toSpend.getCLIString());
                    ConcreteResourceSet set = new ConcreteResourceSet();
                    set.addResource(resource, numOfResources);
                    if (toSpend.contains(set)) {
                        warehouse[depotIndex].addResource(resource, numOfResources);
                        toSpend.removeResource(resource, numOfResources);
                        System.out.println("resource added");
                        System.out.println("toSpend: "+ toSpend.size());
                        if(toSpend.size()==0) {
                            write(client, warehouse, strongbox);
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
