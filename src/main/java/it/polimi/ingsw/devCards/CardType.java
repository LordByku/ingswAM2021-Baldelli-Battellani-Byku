package it.polimi.ingsw.devCards;

import java.util.HashSet;

public class CardType {
    private CardColour colour;
    private HashSet<CardLevel> levelSet;

    public CardType(CardColour colour) throws InvalidCardColourException {
        if(colour == null) {
            throw new InvalidCardColourException();
        }
        this.colour = colour;
        this.levelSet = new HashSet<>();
    }

    public void addLevel(CardLevel cardLevel) throws InvalidCardLevelException {
        if(cardLevel == null) {
            throw new InvalidCardLevelException();
        }
        levelSet.add(cardLevel);
    }

    public boolean isSatisfied(DevCard devCard) {
        return devCard.getColour() == colour && levelSet.contains(devCard.getLevel());
    }
}