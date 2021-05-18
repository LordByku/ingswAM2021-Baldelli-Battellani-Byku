package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.clientStates.ManageWarehouse;
import it.polimi.ingsw.network.client.clientStates.WhiteMarbleSelection;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;

public class SinglePlayerWhiteMarbleSelection extends WhiteMarbleSelection {
    public SinglePlayerWhiteMarbleSelection(ChoiceSet choiceSet, int choices) {
        super(choiceSet, choices);
    }

    @Override
    protected void handleSelection(Client client, ArrayList<ConcreteResource> resources) {
        if(Controller.getInstance().whiteMarble(resources.toArray(new ConcreteResource[0]))) {
            ConcreteResourceSet obtained = Controller.getInstance().concreteToObtain();
            Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();
            CLI.getInstance().showWarehouse(playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards());
            client.setState(ManageWarehouse.builder(obtained, playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards()));
        } else {
            selectionToCLI();
        }
    }
}
