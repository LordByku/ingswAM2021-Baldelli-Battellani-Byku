package it.polimi.ingsw.game.actionTokens;

/**
 * AdvanceTwiceToken is the action token that advances two
 * position in the faith track
 */
public class AdvanceTwiceToken extends ActionToken {
    /**
     * flip adds two faith points to the Computer's Faith Track and returns the
     * same deck without the top token
     * @param deck The current state of the deck
     * @return deck after removing the top token
     */
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        deck.getFaithTrack().addFaithPoints(2);
        deck.removeTopToken();
        return deck;
    }
}
