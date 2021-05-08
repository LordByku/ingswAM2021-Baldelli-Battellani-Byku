package it.polimi.ingsw.model.game.actionTokens;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gameZone.CardMarket;

/**
 * DiscardBlueToken is the action token that discards two
 * blue development cards from the CardMarket
 */
public class DiscardBlueToken extends ActionToken {
    /**
     * flip discards two blue development cards from the CardMarket
     * and returns the same deck without the top token
     * @param deck The current state of the deck
     * @return deck after removing the top token
     */
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.BLUE);
        cardMarket.discardColourCard(CardColour.BLUE);
        deck.removeTopToken();
        return deck;
    }
}
