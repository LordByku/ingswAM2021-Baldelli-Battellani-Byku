package it.polimi.ingsw.game.actionTokens;

public class AdvanceTwiceToken extends ActionToken {
    @Override
    public ActionTokenDeck flip(ActionTokenDeck deck) {
        deck.getBoard().addFaithPoints(2);
        deck.removeTopToken();
        return deck;
    }
}
