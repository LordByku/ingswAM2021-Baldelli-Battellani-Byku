package it.polimi.ingsw.model.devCards;

import java.util.HashSet;

public class CardType {
    /**
     * colour represent the colour of the set
     */
    private final CardColour colour;
    /**
     * levelSet represents the set of level of one kind of colour expressed by the card type
     */
    private final HashSet<CardLevel> levelSet;

    /**
     * the constructor initializes a empty set for the level and colour of the set of levels
     * @param colour the colour of the set
     * @throws InvalidCardColourException colour is null
     */
    public CardType(CardColour colour) throws InvalidCardColourException {
        if(colour == null) {
            throw new InvalidCardColourException();
        }
        this.colour = colour;
        this.levelSet = new HashSet<>();
    }

    /**
     * addLevel represents the action of adding levels to the set
     * @param cardLevel the level of the card to be added
     * @throws InvalidCardLevelException the cardLevel is null
     */
    public void addLevel(CardLevel cardLevel) throws InvalidCardLevelException {
        if(cardLevel == null) {
            throw new InvalidCardLevelException();
        }
        levelSet.add(cardLevel);
    }

    /**
     * @param devCard the devCard to be checked
     * @return true iff the card level and colours are contained in the CardTypeSet
     */
    public boolean isSatisfied(DevCard devCard) {
        return devCard.getColour() == colour && levelSet.contains(devCard.getLevel());
    }
}