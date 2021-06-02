package it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements;

import it.polimi.ingsw.model.devCards.CardColour;

import java.util.Arrays;

public class CardSetRequirements extends Requirements {
    private final CardSet[] cardSet;

    public CardSetRequirements() {
        super(RequirementType.cardSet);
        int colours = CardColour.values().length;
        cardSet = new CardSet[colours];
        for (int i = 0; i < colours; i++) {
            cardSet[i] = new CardSet();
        }
    }

    private int colourIndex(CardColour colour) {
        return Arrays.asList(CardColour.values()).indexOf(colour);
    }

    public CardSet getCardSet(CardColour colour) {
        return cardSet[colourIndex(colour)];
    }
}
