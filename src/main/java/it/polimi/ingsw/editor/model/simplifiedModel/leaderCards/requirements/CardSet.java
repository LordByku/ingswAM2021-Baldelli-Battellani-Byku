package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

public class CardSet {
    private CardColour cardColour;
    private int quantity;
    private CardLevel cardLevel;

    public CardSet(CardColour cardColour) {
        this.cardColour = cardColour;
        quantity = 0;
        cardLevel = null;
    }

    public CardColour getCardColour() {
        return cardColour;
    }

    public void setCardColour(CardColour cardColour) {
        this.cardColour = cardColour;
    }

    public CardLevel getCardLevel() {
        return cardLevel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void toggle(CardLevel cardLevel) {
        if (this.cardLevel == cardLevel) {
            this.cardLevel = null;
        } else {
            this.cardLevel = cardLevel;
        }
    }
}
