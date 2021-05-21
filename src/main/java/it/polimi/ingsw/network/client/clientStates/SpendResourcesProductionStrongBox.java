package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerSpendResourcesStrongbox;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.view.cli.CLI;

public class SpendResourcesProductionStrongBox extends SpendResourcesProduction{
    ConcreteResourceSet[] warehouse;
    ConcreteResourceSet strongbox;
    SpendableResourceSet toSpend;

    public SpendResourcesProductionStrongBox(ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox, SpendableResourceSet toSpend){
       // CLI.getInstance().toSpend(toSpend);
        CLI.getInstance().spendResourcesStrongbox();
        this.warehouse = warehouse;
        this.strongbox = strongbox;
        this.toSpend = toSpend;
    }

    public static ClientState builder(ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox, SpendableResourceSet toSpend) {
        if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
            //return new SinglePlayerSpendResourcesStrongbox(warehouse, strongbox, toSpend);
            return new StartTurn();
        } else {
            return new SpendResourcesProductionStrongBox(warehouse, strongbox, toSpend);
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] arr = line.split(" ");
        if(arr.length==1){
            if(line.equals("warehouse")){
                Board board = client.getModel().getPlayer(client.getNickname()).getBoard();
                int numOfDepots = board.getWarehouse().size();
               // CLI.getInstance().showWarehouse(board.getWarehouse(), board.getPlayedLeaderCards());
                client.setState(SpendResourcesProductionWarehouse.builder(numOfDepots, toSpend));
            } else {
                CLI.getInstance().spendResourcesStrongbox();
            }
        } else {
            try {
                int numOfResources = Integer.parseInt(arr[0]);
                ConcreteResource resource = ClientParser.getInstance().readUserResource(arr[1]);
                ConcreteResourceSet set = new ConcreteResourceSet();
                if(resource!=null && numOfResources>0 && numOfResources <=client.getModel().getPlayer(client.getNickname()).getBoard().getStrongBox().getCount(resource)){
                    set.addResource(resource, numOfResources);
                    if(toSpend.getResourceSet().getConcreteResources().contains(set)) {
                        strongbox.addResource(resource,numOfResources);
                        toSpend.getResourceSet().getConcreteResources().removeResource(resource,numOfResources);
                        if(toSpend.size()==0) {
                            handleSelection(client, warehouse, strongbox);
                        } else {
                           // CLI.getInstance().toSpend(toSpend);
                            CLI.getInstance().spendResourcesStrongbox();
                        }
                    }else if(toSpend.getResourceSet().getChoiceResources().size()>=set.size()) {
                        strongbox.addResource(resource, numOfResources);
                        for(int i =0; i< numOfResources; i++)
                            toSpend.getResourceSet().getChoiceResources().remove(resource);

                        if (toSpend.size() == 0) {
                            handleSelection(client, warehouse, strongbox);
                        } else {
                            // CLI.getInstance().toSpend(toSpend);
                            CLI.getInstance().spendResourcesStrongbox();
                        }
                    }
                    else {
                        CLI.getInstance().spendResourcesStrongbox();
                    }
                } else {
                    CLI.getInstance().spendResourcesStrongbox();
                }
            } catch (NumberFormatException | JsonSyntaxException e) {
               // CLI.getInstance().purchaseDevCard();
            }
        }
    }
}
