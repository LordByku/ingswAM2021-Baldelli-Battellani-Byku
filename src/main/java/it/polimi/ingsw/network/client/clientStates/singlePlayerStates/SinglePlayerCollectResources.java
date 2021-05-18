package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.leaderCards.WhiteConversionLeaderCard;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.clientStates.CollectResources;
import it.polimi.ingsw.network.client.clientStates.ManageWarehouse;
import it.polimi.ingsw.network.client.clientStates.WhiteMarbleSelection;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.network.client.localModel.MarbleMarket;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.cli.CLI;

public class SinglePlayerCollectResources extends CollectResources {
    @Override
    protected void handleSelection(Client client, boolean rowColSel, int index) {
        Person person = Game.getInstance().getSinglePlayer();
        MarbleMarket marbleMarket = client.getModel().getGameZone().getMarbleMarket();
        Board board = client.getModel().getPlayer(client.getNickname()).getBoard();

        if(Controller.getInstance().market(person, rowColSel, index)) {
            if(Controller.getInstance().toObtainIsConcrete()) {
                ConcreteResourceSet obtained = Controller.getInstance().concreteToObtain();
                client.setState(ManageWarehouse.builder(obtained, board.getWarehouse(), board.getPlayedLeaderCards()));
            } else {
                ChoiceSet choiceSet = new ChoiceSet();
                for(Integer leaderCardId: board.getPlayedLeaderCards()) {
                    LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(leaderCardId);
                    if(leaderCard.isType(LeaderCardType.CONVERSION)) {
                        choiceSet.addChoice(((WhiteConversionLeaderCard) leaderCard).getType());
                    }
                }
                int choices = Controller.getInstance().toObtainChoices();
                client.setState(WhiteMarbleSelection.builder(choiceSet, choices));
            }
        } else {
            CLI.getInstance().marbleMarket(marbleMarket);
            CLI.getInstance().marbleMarketSelection();
        }
    }
}
