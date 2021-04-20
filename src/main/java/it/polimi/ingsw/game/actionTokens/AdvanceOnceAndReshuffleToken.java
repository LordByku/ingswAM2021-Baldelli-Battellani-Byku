package it.polimi.ingsw.game.actionTokens;

/**
 * AdvanceOnceAndReshuffleToken is the action token that advances
 * one position in the faith track and reshuffles the deck
 */
public class AdvanceOnceAndReshuffleToken extends ActionToken {
    /**
     * flip adds a faith point to the Computer's Faith Track and returns a new deck
     * @param deck The current state of the deck
     * @return A new ActionTokenDeck
     */
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        deck.getFaithTrack().addFaithPoints();
        return new ActionTokenDeck(deck.getFaithTrack());
    }
}
