package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.ClientState;
import it.polimi.ingsw.network.client.clientStates.EndTurn;
import it.polimi.ingsw.network.client.clientStates.ManageWarehouse;
import it.polimi.ingsw.network.server.GameStateSerializer;

import java.util.ArrayList;

public class SinglePlayerManageWarehouse extends ManageWarehouse {
    public SinglePlayerManageWarehouse(ConcreteResourceSet obtained, ArrayList<ConcreteResourceSet> warehouse, ArrayList<Integer> playedLeaderCards) {
        super(obtained, warehouse, playedLeaderCards);
    }

    @Override
    protected void handleConfirm(Client client) {
        Controller.getInstance().confirmWarehouse(Game.getInstance().getSinglePlayer());
        GameStateSerializer serializer = new GameStateSerializer(client.getNickname());
        serializer.addFaithTrack(Game.getInstance().getSinglePlayer());
        serializer.addMarbleMarket();
        client.getModel().updateModel(serializer.getMessage());
        client.setState(new EndTurn());
    }
}
