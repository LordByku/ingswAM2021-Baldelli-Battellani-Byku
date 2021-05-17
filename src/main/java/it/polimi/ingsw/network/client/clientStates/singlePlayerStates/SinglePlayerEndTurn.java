package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.clientStates.EndTurn;
import it.polimi.ingsw.network.server.GameStateSerializer;

public class SinglePlayerEndTurn extends EndTurn {

    @Override
    protected void handleSelection(Client client){
        Person person = Game.getInstance().getSinglePlayer();
        Controller.getInstance().endTurn(person);
        //TODO
        //GameStateSerializer serializer = new GameStateSerializer(person.getNickname());
        //serializer.addPlayerDetails(person);
        //client.getModel().updateModel(serializer.getMessage());
        //client.setState();
    }
}
