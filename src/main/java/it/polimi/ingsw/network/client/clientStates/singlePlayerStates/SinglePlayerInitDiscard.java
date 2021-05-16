package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.InitDiscard;
import it.polimi.ingsw.network.client.clientStates.InitResources;
import it.polimi.ingsw.network.client.clientStates.StartTurn;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.view.cli.CLI;

public class SinglePlayerInitDiscard extends InitDiscard {
    public SinglePlayerInitDiscard(int maxSelection) {
        super(maxSelection);
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    protected void handleSelection(Client client, int indexA, int indexB) {
        int[] indices = new int[2];
        indices[0] = indexA;
        indices[1] = indexB;
        if(Controller.getInstance().initDiscard(Game.getInstance().getSinglePlayer(), indices)) {
            GameStateSerializer serializer = new GameStateSerializer(client.getNickname());
            serializer.addHandLeaderCards(Game.getInstance().getSinglePlayer());
            client.getModel().updateModel(serializer.getMessage());
            client.setState(new StartTurn());
        } else {
            CLI.getInstance().initDiscard();
        }
    }
}
