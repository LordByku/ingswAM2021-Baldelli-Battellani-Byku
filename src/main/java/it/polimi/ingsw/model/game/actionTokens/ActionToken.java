package it.polimi.ingsw.model.game.actionTokens;

/**
 * ActionToken represents action tokens in single player games
 */
public abstract class ActionToken {
    /**
     * flip executes the action of this token
     * @param deck The current state of the deck
     * @return The new deck after executing the action
     */
    public abstract ActionTokenDeck flip(ActionTokenDeck deck);
}
