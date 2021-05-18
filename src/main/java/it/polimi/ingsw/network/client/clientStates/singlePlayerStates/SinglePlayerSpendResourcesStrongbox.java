package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.clientStates.EndTurn;
import it.polimi.ingsw.network.client.clientStates.SpendResourcesStrongbox;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.view.cli.CLI;

public class SinglePlayerSpendResourcesStrongbox extends SpendResourcesStrongbox {
    int deckIndex;

    public SinglePlayerSpendResourcesStrongbox(ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox, ConcreteResourceSet toSpend, int deckIndex) {
        super(warehouse, strongbox, toSpend, deckIndex);
        this.deckIndex=deckIndex;
    }

    @Override
    public void handleSelection(Client client, ConcreteResourceSet[] warehouse, ConcreteResourceSet strongbox) {
        Person person = Game.getInstance().getSinglePlayer();
        if(Controller.getInstance().spendResources(Game.getInstance().getSinglePlayer(),deckIndex,warehouse,strongbox)){
            CLI.getInstance().spendResourcesSuccess();
            GameStateSerializer serializer = new GameStateSerializer(client.getNickname());
            serializer.addDevCards(person);
            serializer.addWarehouse(person);
            serializer.addStrongbox(person);
            serializer.addCardMarket();
            client.getModel().updateModel(serializer.getMessage());
            client.setState(new EndTurn());
        }
    }
}
