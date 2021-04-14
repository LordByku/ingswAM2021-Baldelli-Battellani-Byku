package it.polimi.ingsw.game.actionTokens;

public class DiscardPurpleToken extends ActionToken {
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        // TODO: discard two purple dev cards
        deck.removeTopToken();
        return deck;
    }
}
