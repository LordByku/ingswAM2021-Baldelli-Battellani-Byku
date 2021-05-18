package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.clientStates.AddToDepot;
import it.polimi.ingsw.network.client.clientStates.ManageWarehouse;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.view.cli.CLI;

public class SinglePlayerAddToDepot extends AddToDepot {
    public SinglePlayerAddToDepot(ConcreteResourceSet obtained) {
        super(obtained);
    }

    @Override
    protected void handleSelection(Client client, int depotIndex, ConcreteResourceSet toAdd) {
        Person person = Game.getInstance().getSinglePlayer();
        if(Controller.getInstance().addResourcesToDepot(person, toAdd, depotIndex)) {
            GameStateSerializer serializer = new GameStateSerializer(client.getNickname());
            serializer.addWarehouse(person);
            System.out.println(serializer.getMessage());
            client.getModel().updateModel(serializer.getMessage());
            ConcreteResourceSet obtained = Controller.getInstance().concreteToObtain();
            Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();
            client.setState(ManageWarehouse.builder(obtained, playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards()));
        } else {
            CLI.getInstance().addToDepot();
        }
    }
}
