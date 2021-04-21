package it.polimi.ingsw.model.game.actionTokens;

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
        // TODO: discard two green dev cards
        deck.removeTopToken();
        return deck;
    }
}
