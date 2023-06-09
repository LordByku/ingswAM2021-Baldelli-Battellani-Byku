package it.polimi.ingsw.model.gameZone;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.DevCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


/**
 * Deck of DevCards of the CardMarket
 */
public class CardMarketDeck {

    /**
     * The colour of the devCards in the deck.
     */
    private final CardColour colour;

    /**
     * The level of the devCards in the deck.
     */
    private final CardLevel level;

    /**
     * The deck.
     */
    private final Stack<DevCard> devCardStack;

    /**
     * Constructor creates a new stack and initialize attributes.
     */
    public CardMarketDeck(CardColour colour, CardLevel level) {
        this.colour = colour;
        this.level = level;

        devCardStack = new Stack<>();
    }

    /**
     * Getter for attribute colour.
     *
     * @return The value of the attribute colour.
     */
    public CardColour getColour() {
        return colour;
    }

    /**
     * Getter for attribute level.
     *
     * @return The value of the attribute level.
     */
    public CardLevel getLevel() {
        return level;
    }

    /**
     * Appends a devCard to the end of the Deck
     *
     * @param devCard The card to be added
     */
    public void appendToDeck(DevCard devCard) {
        devCardStack.add(devCard);
    }

    /**
     * Removes the card at the top of the Deck.
     *
     * @return The devCard removed.
     */
    public DevCard removeTop() throws EmptyDeckException {
        if (isEmpty()) {
            throw new EmptyDeckException();
        }
        return devCardStack.pop();
    }

    /**
     * @return The devCard at the top of the deck
     */
    public DevCard top() throws EmptyDeckException {
        if (isEmpty()) {
            throw new EmptyDeckException();
        }
        return devCardStack.peek();
    }

    /**
     * isEmpty checks whether this deck is empty
     * @return true iff this deck is empty
     */
    public boolean isEmpty() {
        return devCardStack.isEmpty();
    }

    /**
     * Shuffle the deck.
     */
    public void shuffleDeck() {
        ArrayList<DevCard> arrayList = new ArrayList<>();
        while (!devCardStack.isEmpty()) {
            arrayList.add(devCardStack.pop());
        }

        Collections.shuffle(arrayList);

        for (int i = 0; i < arrayList.size(); i++) {
            devCardStack.add(i, arrayList.get(i));
        }
    }

    /**
     * size returns the size of this deck
     * @return the size of this deck
     */
    public int size() {
        return devCardStack.size();
    }
}
