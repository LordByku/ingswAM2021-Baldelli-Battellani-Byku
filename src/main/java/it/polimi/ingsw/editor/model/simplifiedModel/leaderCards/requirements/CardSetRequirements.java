package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import it.polimi.ingsw.model.devCards.CardColour;

import java.util.Arrays;

public class CardSetRequirements extends Requirements {
    private final CardSet[] cardSets;

    public CardSetRequirements() {
        super(RequirementType.cardSet);
        int colours = CardColour.values().length;
        cardSets = new CardSet[colours];
        for (int i = 0; i < colours; i++) {
            cardSets[i] = new CardSet(CardColour.values()[i]);
        }
    }

    private int colourIndex(CardColour colour) {
        return Arrays.asList(CardColour.values()).indexOf(colour);
    }

    public CardSet getCardSet(CardColour colour) {
        return cardSets[colourIndex(colour)];
    }

    public void setCardSet(CardSet cardSet) {
        cardSets[colourIndex(cardSet.getCardColour())] = cardSet;
    }
}
