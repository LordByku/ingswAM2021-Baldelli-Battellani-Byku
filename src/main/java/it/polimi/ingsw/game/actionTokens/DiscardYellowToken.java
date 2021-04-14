package it.polimi.ingsw.game.actionTokens;

public class DiscardYellowToken extends ActionToken {
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        // TODO: discard two yellow dev cards
        deck.removeTopToken();
        return deck;
    }
}
