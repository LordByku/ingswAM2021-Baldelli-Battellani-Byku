package it.polimi.ingsw.model.game.actionTokens;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.gameZone.CardMarket;
import it.polimi.ingsw.model.playerBoard.faithTrack.VRSObserver;
import it.polimi.ingsw.view.cli.CLIPrintable;
import it.polimi.ingsw.view.cli.TextColour;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.function.UnaryOperator;

public enum ActionToken implements CLIPrintable {
    ADVANCEONCEANDRESHUFFLE(deck -> {
        deck.getFaithTrack().addFaithPoints();
        VRSObserver.getInstance().updateVRS();
        return new ActionTokenDeck(deck.getFaithTrack());
    }, "Lorenzo il Magnifico advanced one position in his Faith Track and shuffled all the tokens", "advanceonceandshuffle"),
    ADVANCETWICE(deck -> {
        deck.getFaithTrack().addFaithPoints(2);
        VRSObserver.getInstance().updateVRS();
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico advanced two positions in his Faith Track", "advancetwice"),
    DISCARDBLUE(deck -> {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.BLUE);
        cardMarket.discardColourCard(CardColour.BLUE);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico discarded two blue development cards", "discardblue"),
    DISCARDGREEN(deck -> {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.GREEN);
        cardMarket.discardColourCard(CardColour.GREEN);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico discarded two green development cards", "discardgreen"),
    DISCARDPURPLE(deck -> {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.PURPLE);
        cardMarket.discardColourCard(CardColour.PURPLE);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico discarded two purple development cards", "discardpurple"),
    DISCARDYELLOW(deck -> {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        cardMarket.discardColourCard(CardColour.YELLOW);
        cardMarket.discardColourCard(CardColour.YELLOW);
        deck.removeTopToken();
        return deck;
    }, "Lorenzo il Magnifico discarded two yellow development cards", "discardyellow");

    private final String cliString;
    private final UnaryOperator<ActionTokenDeck> flipLambda;
    private Image image;
    private final String filename;
    ActionToken(UnaryOperator<ActionTokenDeck> flipLambda, String cliString, String name) {
        this.flipLambda = flipLambda;
        this.cliString = cliString;
        this.filename = name;
    }

    public void loadImage() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("Punchboard/" + filename + ".png");
        image = ImageIO.read(resource);
    }

    public ActionTokenDeck flip(ActionTokenDeck deck) {
        return flipLambda.apply(deck);
    }

    @Override
    public String getCLIString() {
        return TextColour.ORANGE.escape() + cliString + TextColour.RESET;
    }

    public String getString() {
        return cliString;
    }

    public Image getImage() {
        return image;
    }

}
