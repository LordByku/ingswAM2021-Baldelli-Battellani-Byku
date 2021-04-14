package it.polimi.ingsw.game.actionTokens;

public class DiscardBlueToken extends ActionToken {
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        // TODO: discard two blue dev cards
        deck.removeTopToken();
        return deck;
    }
}
