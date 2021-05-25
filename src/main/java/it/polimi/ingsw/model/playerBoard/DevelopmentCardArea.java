package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.parsing.BoardParser;

import java.util.ArrayList;

/**
 * DevelopmentCardArea is the container for development cards
 * in a Board
 */
public class DevelopmentCardArea implements Scoring {
    /**
     * decks contains the decks of development cards in
     * this DevelopmentCardArea
     */
    private final ArrayList<DevCardDeck> decks;

    /**
     * The constructor initializes decks to contain 3 empty decks
     */
    public DevelopmentCardArea() {
        int developmentCardsSlots = BoardParser.getInstance().getDevelopmentCardsSlots();
        decks = new ArrayList<>();
        for (int i = 0; i < developmentCardsSlots; ++i) {
            decks.add(new DevCardDeck());
        }
    }

    /**
     * getPoints returns the sum of points of DevCards in this DevelopmentCardArea
     *
     * @return The points awarded by all DevCards in this DevelopmentCardArea
     */
    @Override
    public int getPoints() {
        int points = 0;
        for (DevCardDeck deck : decks) {
            points += deck.getPoints();
        }
        return points;
    }

    /**
     * getTopLevel returns the CardLevel of the top DevCard in a given deck
     *
     * @param deckIndex The deck to get the CardLevel from
     * @return The CardLevel of the DevCard on top of the given deck,
     * or null if such deck is empty
     * @throws InvalidDevCardDeckException deckIndex is outside the range of decks
     */
    public CardLevel getTopLevel(int deckIndex) throws InvalidDevCardDeckException {
        if (deckIndex < 0 || deckIndex >= decks.size()) {
            throw new InvalidDevCardDeckException();
        }

        return decks.get(deckIndex).topLevel();
    }

    /**
     * addDevCard adds a DevCard on top of a given deck
     *
     * @param devCard   The DevCard to add
     * @param deckIndex The index of the deck to add the card in
     * @throws InvalidDevCardException     devCard is null
     * @throws InvalidDevCardDeckException deckIndex is outside the range of decks
     * @throws InvalidAddTopException      The card cannot be added in the given deck
     */
    public void addDevCard(DevCard devCard, int deckIndex)
            throws InvalidDevCardException, InvalidDevCardDeckException, InvalidAddTopException {
        if (deckIndex < 0 || deckIndex >= decks.size()) {
            throw new InvalidDevCardDeckException();
        }

        decks.get(deckIndex).add(devCard);
    }

    /**
     * getCards returns an ArrayList of all DevCards contained
     * in this DevelopmentCardArea
     *
     * @return An ArrayList containing all DevCards in this DevelopmentCardArea
     */
    public ArrayList<DevCard> getCards() {
        ArrayList<DevCard> result = new ArrayList<>();
        for (DevCardDeck deck : decks) {
            result.addAll(deck.getCards());
        }
        return result;
    }

    public ArrayList<DevCardDeck> getDecks() {
        return (ArrayList<DevCardDeck>) decks.clone();
    }
}
