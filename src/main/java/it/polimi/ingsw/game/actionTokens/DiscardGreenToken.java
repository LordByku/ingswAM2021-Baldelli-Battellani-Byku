package it.polimi.ingsw.game.actionTokens;

public class DiscardGreenToken extends ActionToken {
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        // TODO: discard two green dev cards
        deck.removeTopToken();
        return deck;
    }
}
