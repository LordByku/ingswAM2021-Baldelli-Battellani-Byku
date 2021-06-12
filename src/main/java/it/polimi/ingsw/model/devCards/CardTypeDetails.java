package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;
import it.polimi.ingsw.view.cli.BackGroundColor;

public class CardTypeDetails {
    private final CardColour cardColour;
    private final int quantity;
    private final CardLevel cardLevel;

    public CardTypeDetails(CardColour cardColour, int quantity) throws InvalidQuantityException, InvalidCardColourException {
        if (cardColour == null) {
            throw new InvalidCardColourException();
        }
        if (quantity < 0) {
            throw new InvalidQuantityException();
        }
        this.cardColour = cardColour;
        this.quantity = quantity;
        this.cardLevel = null;
    }

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

    public CardColour getCardColour() {
        return cardColour;
    }

    public int getQuantity() {
        return quantity;
    }

    public CardLevel getCardLevel() {
        return cardLevel;
    }

    public boolean isSatisfied(DevCard card) {
        return cardColour == card.getColour() && (cardLevel == null || cardLevel == card.getLevel());
    }

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