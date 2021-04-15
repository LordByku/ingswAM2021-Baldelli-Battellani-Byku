package it.polimi.ingsw.devCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.Scoring;
import it.polimi.ingsw.resources.InvalidResourceException;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

import java.util.HashSet;

/**
 * DevCard represents development cards
 */

public abstract class DevCard implements Scoring {


    /**
     * reqResources represents the set of resources required to buy the card
     */
    ConcreteResourceSet reqResources;
    /**
     * colour represents the card's colour
     */
    CardColour colour;
    /**
     * level represents the card's level
     */
    CardLevel level;
    /**
     * productionPower represents the production details of the card
     */
    ProductionDetails productionPower;
    /**
     * points represents the card's points
     */
    int points;

    /**
     * The constructor
     * @param reqResources set of resources required to buy the card
     * @param colour the colour to be set
     */
    DevCard(ConcreteResourceSet reqResources,CardColour colour)throws InvalidResourceSetException,InvalidCardColour {
        if(reqResources==null)
            throw new InvalidResourceSetException();
        if(colour==null)
            throw new InvalidCardColour();
        this.reqResources = (ConcreteResourceSet) reqResources.clone();
        this.colour = colour;

        }

    public void buy(Board board) {}

    public ConcreteResourceSet getReqResources() {
        return reqResources;
    }

    /**
     * getLevel() is a getter of the class
     * @return the level of the development card
     */
    public CardLevel getLevel() {
        return level;
    }

    /**
     * @return the colour of the development card
     */
    public CardColour getColour() {

        return colour;
    }

   public CardType getCardType(){
        HashSet<CardLevel> levels = new HashSet<>();
        levels.add(this.getLevel());
       return new CardType(this.getColour(),levels);
    }

    /**
     * getPoints is a getter of card's points
     * @return points of the card
     */
    public int getPoints() {
        return points;
    }
}
