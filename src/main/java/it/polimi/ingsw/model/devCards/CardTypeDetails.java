package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;
import it.polimi.ingsw.view.cli.BackGroundColor;
import it.polimi.ingsw.view.cli.CLIPrintable;

/**
 * CardTypeDetails is the class for a type of DevCard requirements
 */
public class CardTypeDetails implements CLIPrintable {
    /**
     * cardColour is the required CardColour
     */
    private final CardColour cardColour;
    /**
     * quantity is the required quantity
     */
    private final int quantity;
    /**
     * cardLevel is the required CardLevel or null if no specific level is required
     */
    private final CardLevel cardLevel;

    /**
     * This constructor sets the default value for cardLevel (null)
     * @param cardColour the required CardColour
     * @param quantity the required quantity
     * @throws InvalidQuantityException quantity is negative
     * @throws InvalidCardColourException cardColour is null
     */
    public CardTypeDetails(CardColour cardColour, int quantity) throws InvalidQuantityException, InvalidCardColourException {
        this(cardColour, quantity, null);
    }

    /**
     * Default constructor
     * @param cardColour the required CardColour
     * @param quantity the required quantity
     * @param cardLevel the required CardLevel
     * @throws InvalidQuantityException quantity is negative
     * @throws InvalidCardColourException cardColour is null
     */
    public CardTypeDetails(CardColour cardColour, int quantity, CardLevel cardLevel) throws InvalidQuantityException, InvalidCardColourException {
        if (cardColour == null) {
            throw new InvalidCardColourException();
        }
        if (quantity < 0) {
            throw new InvalidQuantityException();
        }
        this.cardColour = cardColour;
        this.quantity = quantity;
        this.cardLevel = cardLevel;
    }

    /**
     * getCardColour gets the required CardColour
     * @return the required CardColour
     */
    public CardColour getCardColour() {
        return cardColour;
    }

    /**
     * getQuantity gets the required quantity
     * @return the required quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * getCardLevel gets the required CardLevel
     * @return the required CardLevel
     */
    public CardLevel getCardLevel() {
        return cardLevel;
    }

    /**
     * isSatisfied checks if this card type is satisfied by a given DevCard
     * @param card the given DevCard
     * @return true if the given DevCard satisfies the CardColour and CardLevel requirements
     */
    public boolean isSatisfied(DevCard card) {
        return cardColour == card.getColour() && (cardLevel == null || cardLevel == card.getLevel());
    }

    @Override
    public String getCLIString() {
        StringBuilder result = new StringBuilder("" + quantity);
        if (cardLevel == null) {
            result.append("|").append(cardColour.getColour().escape()).append(" ").append(BackGroundColor.RESET).append("|");
        } else {
            result.append("|").append(cardColour.getColour().escape());
            result.append(cardLevel.getCLIString());
            result.append(BackGroundColor.RESET + "|");
        }
        return result.toString();
    }
}