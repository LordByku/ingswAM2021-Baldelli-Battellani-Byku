package it.polimi.ingsw.model.gameZone;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.parsing.DevCardsParser;

import java.util.Arrays;
import java.util.List;

/**
 * This class refers to the Card Market where you can buy the devCards.
 */
public class CardMarket {
    /**
     * An ArrayList of the different decks of devCards of the devCardMarket.
     */
    private final CardMarketDeck[][] decks;

    /**
     * The constructor creates and adds the CardMarketDecks to the ArrayList.
     */
    public CardMarket() {
        decks = new CardMarketDeck[CardLevel.values().length][CardColour.values().length];

        for (int i = 0; i < CardLevel.values().length; ++i) {
            for (int j = 0; j < CardColour.values().length; ++j) {
                decks[i][j] = new CardMarketDeck(CardColour.values()[j], CardLevel.values()[i]);
            }
        }

        List<CardLevel> cardLevelList = Arrays.asList(CardLevel.values());
        List<CardColour> cardColourList = Arrays.asList(CardColour.values());

        DevCard devCard;
        while ((devCard = DevCardsParser.getInstance().getNextCard()) != null) {
            int levelRow = cardLevelList.indexOf(devCard.getLevel());
            int colourColumn = cardColourList.indexOf(devCard.getColour());

            decks[levelRow][colourColumn].appendToDeck(devCard);
        }

        for (int i = 0; i < CardLevel.values().length; ++i) {
            for (int j = 0; j < CardColour.values().length; ++j) {
                decks[i][j].shuffleDeck();
            }
        }
    }

    /**
     * top returns the top card of a given CardMarket deck
     * @param levelRow the market row
     * @param colourColumn the market column
     * @return the DevCard on top of the selected deck
     */
    public DevCard top(int levelRow, int colourColumn) {
        if (levelRow < 0 || levelRow >= CardLevel.values().length ||
                colourColumn < 0 || colourColumn >= CardColour.values().length) {
            throw new InvalidCardMarketIndexException();
        }
        return decks[levelRow][colourColumn].top();
    }

    /**
     * removeTop removes and returns the top card of a given CardMarket deck
     * @param levelRow the market row
     * @param colourColumn the market column
     * @return the DevCard removed from the selected deck
     * @throws EmptyDeckException if the selected deck is empty
     */
    public DevCard removeTop(int levelRow, int colourColumn) throws EmptyDeckException {
        if (levelRow < 0 || levelRow >= CardLevel.values().length ||
                colourColumn < 0 || colourColumn >= CardColour.values().length) {
            throw new InvalidCardMarketIndexException();
        }
        DevCard card = decks[levelRow][colourColumn].removeTop();
        if (Game.getInstance().getNumberOfPlayers() == 1 && hasEmptyColour()) {
            Game.getInstance().setLastTurn();
        }

        return card;
    }

    /**
     * size returns the number of DevCards in a given CardMarket deck
     * @param levelRow the market row
     * @param colourColumn the market column
     * @return the size of the selected deck
     */
    public int size(int levelRow, int colourColumn) {
        if (levelRow < 0 || levelRow >= CardLevel.values().length ||
                colourColumn < 0 || colourColumn >= CardColour.values().length) {
            throw new InvalidCardMarketIndexException();
        }
        return decks[levelRow][colourColumn].size();
    }

    /**
     * discardColourCard discards a DevCard of a given colour,
     * starting from the ones of a lower level
     * @param colour the CardColour of the card to discard
     */
    public void discardColourCard(CardColour colour) {
        List<CardColour> cardColourList = Arrays.asList(CardColour.values());
        int j = cardColourList.indexOf(colour);
        for (int i = 0; i < CardLevel.values().length; ++i) {
            if (!decks[i][j].isEmpty()) {
                removeTop(i, j);
                return;
            }
        }
    }

    /**
     * hasEmptyColour checks whether there is at least a CardColour
     * with no card left in this CardMarket
     * @return true iff there is at least a CardColour with no card left
     */
    public boolean hasEmptyColour() {
        for (int i = 0; i < CardColour.values().length; ++i) {
            boolean allEmpty = true;
            for (int j = 0; j < CardLevel.values().length; ++j) {
                if (size(j, i) > 0) {
                    allEmpty = false;
                }
            }
            if (allEmpty) {
                return true;
            }
        }
        return false;
    }
}
