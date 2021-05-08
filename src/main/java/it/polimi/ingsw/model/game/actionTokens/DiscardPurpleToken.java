package it.polimi.ingsw.model.game.actionTokens;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gameZone.CardMarket;

/**
 * DiscardPurpleToken is the action token that discards two
 * purple development cards from the CardMarket
 */
public class DiscardPurpleToken extends ActionToken {
    /**
     * flip discards two purple development cards from the CardMarket
     * and returns the same deck without the top token
     * @param deck The current state of the deck
     * @return deck after removing the top token
     */
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.PURPLE);
        cardMarket.discardColourCard(CardColour.PURPLE);
        deck.removeTopToken();
        return deck;
    }
}
