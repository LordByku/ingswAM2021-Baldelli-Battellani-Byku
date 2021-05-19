package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerSpendResourcesStrongbox;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerSpendResourcesWarehouse;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.view.cli.CLI;

public class SpendResourcesStrongbox extends SpendResources {
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

    public static ClientState builder(ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox, ConcreteResourceSet toSpend, int deckIndex) {
        if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
            return new SinglePlayerSpendResourcesStrongbox(warehouse, strongbox, toSpend, deckIndex);
        } else {
            return new SpendResourcesStrongbox(warehouse, strongbox, toSpend, deckIndex);
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] arr = line.split(" ");
        if(arr.length==1){
            if(line.equals("warehouse")){
                Board board = client.getModel().getPlayer(client.getNickname()).getBoard();
                int numOfDepots = board.getWarehouse().size();
                CLI.getInstance().showWarehouse(board.getWarehouse(), board.getPlayedLeaderCards());
                client.setState(SpendResourcesWarehouse.builder(numOfDepots, toSpend, deckIndex));
            } else {
                CLI.getInstance().spendResourcesStrongbox();
            }
        } else {
            try {
                int numOfResources = Integer.parseInt(arr[0]);
                ConcreteResource resource = ClientParser.getInstance().readUserResource(arr[1]);
                if(resource!=null && numOfResources>0 && numOfResources <=client.getModel().getPlayer(client.getNickname()).getBoard().getStrongBox().getCount(resource)){
                    ConcreteResourceSet set = new ConcreteResourceSet();
                    set.addResource(resource, numOfResources);
                    if(toSpend.contains(set)) {
                        strongbox.addResource(resource,numOfResources);
                        toSpend.removeResource(resource,numOfResources);
                        if(toSpend.size()==0) {
                            handleSelection(client, warehouse, strongbox);
                        } else {
                            CLI.getInstance().toSpend(toSpend);
                            CLI.getInstance().spendResourcesStrongbox();
                        }
                    } else {
                        CLI.getInstance().spendResourcesStrongbox();
                    }
                } else {
                    CLI.getInstance().spendResourcesStrongbox();
                }
            } catch (NumberFormatException | JsonSyntaxException e) {
                CLI.getInstance().purchaseDevCard();
            }
        }
    }
}
