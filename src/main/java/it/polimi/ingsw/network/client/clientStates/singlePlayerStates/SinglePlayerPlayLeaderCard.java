package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.clientStates.ClientState;
import it.polimi.ingsw.network.client.clientStates.PlayLeaderCard;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.view.cli.CLI;

import java.util.function.Supplier;

public class SinglePlayerPlayLeaderCard extends PlayLeaderCard {
    public SinglePlayerPlayLeaderCard(Supplier<ClientState> returnStateSupplier) {
        super(returnStateSupplier);
    }

    @Override
    protected void handleSelection(Client client, int selection) {
        Person person = Game.getInstance().getSinglePlayer();
        if(Controller.getInstance().playLeaderCard(person, selection)) {
            GameStateSerializer serializer = new GameStateSerializer(client.getNickname());
            serializer.addHandLeaderCards(person);
            serializer.addPlayedLeaderCards(person);
            client.getModel().updateModel(serializer.getMessage());
            CLI.getInstance().playLeaderCardSuccess();
            CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
        }
        CLI.getInstance().playLeaderCard();
    }
}
