package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerSpendResourcesWarehouse;
import it.polimi.ingsw.view.cli.CLI;

public class SpendResourcesStrongbox extends SpendResources{
    ConcreteResourceSet[] warehouse;
    ConcreteResourceSet strongbox;
    ConcreteResourceSet toSpend;
    int deckIndex;

    public SpendResourcesStrongbox(ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox, ConcreteResourceSet toSpend, int deckIndex){
        CLI.getInstance().toSpend(toSpend);
        CLI.getInstance().spendResourcesStrongbox();
        this.warehouse = warehouse;
        this.strongbox = strongbox;
        this.toSpend = toSpend;
        this.deckIndex = deckIndex;
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] arr = line.split(" ");
        if(arr.length==1){
            if(line.equals("warehouse")){
                int numOfDepots = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().size();
                if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
                    client.setState(new SinglePlayerSpendResourcesWarehouse(numOfDepots,toSpend, deckIndex));
                }
                else {
                    client.setState(new SpendResourcesWarehouse(numOfDepots, toSpend, deckIndex));
                }
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
                            handleSelection(client, warehouse, strongbox);
                        }
                        else{
                            CLI.getInstance().toSpend(toSpend);
                            CLI.getInstance().spendResourcesStrongbox();
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
