package it.polimi.ingsw.game.actionTokens;

public class AdvanceOnceAndReshuffleToken extends ActionToken {
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        deck.getBoard().addFaithPoints();
        return new ActionTokenDeck(deck.getBoard());
    }
}
