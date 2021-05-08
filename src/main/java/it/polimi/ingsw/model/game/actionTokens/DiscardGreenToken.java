package it.polimi.ingsw.model.game.actionTokens;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gameZone.CardMarket;

/**
 * DiscardGreenToken is the action token that discards two
 * green development cards from the CardMarket
 */
public class DiscardGreenToken extends ActionToken {
    /**
     * flip discards two green development cards from the CardMarket
     * and returns the same deck without the top token
     * @param deck The current state of the deck
     * @return deck after removing the top token
     */
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.GREEN);
        cardMarket.discardColourCard(CardColour.GREEN);
        deck.removeTopToken();
        return deck;
    }
}
