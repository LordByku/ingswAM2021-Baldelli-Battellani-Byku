package it.polimi.ingsw.model.game.actionTokens;

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
        // TODO: discard two purple dev cards
        deck.removeTopToken();
        return deck;
    }
}
