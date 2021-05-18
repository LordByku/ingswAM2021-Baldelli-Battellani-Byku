package it.polimi.ingsw.network.client.clientStates.singlePlayerStates;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.clientStates.PurchaseDevCard;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.cli.CLI;

public class SinglePlayerPurchaseDevCard extends PurchaseDevCard {
    @Override
    protected void handleSelection(Client client, int rowIndex, int columnIndex, int deckIndex){
        Person person = Game.getInstance().getSinglePlayer();
        if(Controller.getInstance().purchase(person, rowIndex, columnIndex, deckIndex)) {
            CLI.getInstance().purchaseDevCardSuccess();
            Board playerBoard = client.getModel().getPlayer(client.getNickname()).getBoard();
            CLI.getInstance().showWarehouseAndStrongbox(playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards(), playerBoard.getStrongBox());
            int numOfDepots = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse().size();
            int cardId = client.getModel().getGameZone().getCardMarket().getDevCard(rowIndex,columnIndex);
            ConcreteResourceSet set = DevCardsParser.getInstance().getCard(cardId).getReqResources();
            client.setState(new SinglePlayerSpendResourcesWarehouse(numOfDepots,set, deckIndex));
        }
    }
}
