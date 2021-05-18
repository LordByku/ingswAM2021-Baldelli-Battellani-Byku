package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.clientStates.EndTurn;
import it.polimi.ingsw.network.client.clientStates.PurchaseDevCard;
import it.polimi.ingsw.network.server.GameStateSerializer;

public class SinglePlayerPurchaseDevCard extends PurchaseDevCard {

    @Override
    protected void handleSelection(Client client, int rowIndex, int columnIndex, int deckIndex){
        Person person = Game.getInstance().getSinglePlayer();
        if(Controller.getInstance().purchase(person, rowIndex, columnIndex, deckIndex)){
            int numOfDepots = person.getBoard().getWarehouse().numberOfDepots();
            ConcreteResourceSet[] warehouse = new ConcreteResourceSet[numOfDepots];
            for (int i=0; i<numOfDepots; ++i){
                warehouse[i]=person.getBoard().getWarehouse().getDepotResources(i);
            }
            ConcreteResourceSet strongbox = person.getBoard().getStrongBox().getResources();
            if(Controller.getInstance().spendResources(person,deckIndex,warehouse,strongbox)){
                GameStateSerializer serializer = new GameStateSerializer(person.getNickname());
                serializer.addDevCards(person);
                serializer.addWarehouse(person);
                serializer.addStrongbox(person);
                serializer.addCardMarket();
                client.getModel().updateModel(serializer.getMessage());
                client.setState(new EndTurn());
            }
        }
    }
}
