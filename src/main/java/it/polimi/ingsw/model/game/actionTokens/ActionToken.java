package it.polimi.ingsw.model.game.actionTokens;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gameZone.CardMarket;
import it.polimi.ingsw.view.cli.CLIPrintable;
import it.polimi.ingsw.view.cli.TextColour;

import java.util.function.UnaryOperator;

public enum ActionToken implements CLIPrintable {
    ADVANCEONCEANDRESHUFFLE(deck -> {
        deck.getFaithTrack().addFaithPoints();
        return new ActionTokenDeck(deck.getFaithTrack());
    }, "Lorenzo il Magnifico advanced one position in his Faith Track and shuffled all the tokens"),
    ADVANCETWICE(deck -> {
        deck.getFaithTrack().addFaithPoints(2);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico advanced two positions in his Faith Track"),
    DISCARDBLUE(deck -> {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.BLUE);
        cardMarket.discardColourCard(CardColour.BLUE);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico discarded two blue development cards"),
    DISCARDGREEN(deck -> {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.GREEN);
        cardMarket.discardColourCard(CardColour.GREEN);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico discarded two green development cards"),
    DISCARDPURPLE(deck -> {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.PURPLE);
        cardMarket.discardColourCard(CardColour.PURPLE);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico discarded two purple development cards"),
    DISCARDYELLOW(deck -> {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.YELLOW);
        cardMarket.discardColourCard(CardColour.YELLOW);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico discarded two yellow development cards");

    private final String cliString;
    private final UnaryOperator<ActionTokenDeck> flipLambda;

    ActionToken(UnaryOperator<ActionTokenDeck> flipLambda, String cliString) {
        this.flipLambda = flipLambda;
        this.cliString = cliString;
    }

    public ActionTokenDeck flip(ActionTokenDeck deck) {
        return flipLambda.apply(deck);
    }

    @Override
    public String getCLIString() {
        return TextColour.ORANGE.escape() + cliString + TextColour.RESET;
    }
}
