package it.polimi.ingsw.model.game.actionTokens;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gameZone.CardMarket;

/**
 * YellowGreenToken is the action token that discards two
 * yellow development cards from the CardMarket
 */
public class DiscardYellowToken extends ActionToken {
    /**
     * flip discards two yellow development cards from the CardMarket
     * and returns the same deck without the top token
     * @param deck The current state of the deck
     * @return deck after removing the top token
     */
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.YELLOW);
        cardMarket.discardColourCard(CardColour.YELLOW);
        deck.removeTopToken();
        return deck;
    }
}
