package it.polimi.ingsw.devCards;

import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.playerBoard.Scoring;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

/**
 * DevCard represents development cards
 */

public class DevCard implements Scoring, Cloneable {
    /**
     * reqResources represents the set of resources required to buy the card
     */
    private ConcreteResourceSet reqResources;
    /**
     * colour represents the card's colour
     */
    private CardColour colour;
    /**
     * level represents the card's level
     */
    private CardLevel level;
    /**
     * productionPower represents the production details of the card
     */
    private ProductionDetails productionPower;
    /**
     * points represents the card's points
     */
    int points;

    /**
     * The constructor
     * @param reqResources set of resources required to buy the card
     * @param colour the colour to be set
     */
    public DevCard(ConcreteResourceSet reqResources, CardColour colour, CardLevel level, ProductionDetails productionPower,int points)
            throws InvalidCardColourException, InvalidCardLevelException, InvalidResourceSetException {
        if(reqResources == null) {
            throw new InvalidResourceSetException();
        }
        if(colour == null) {
            throw new InvalidCardColourException();
        }
        if(level == null) {
            throw new InvalidCardLevelException();
        }
        if(productionPower == null) {
            throw new InvalidProductionDetailsException();
        }
        this.reqResources = (ConcreteResourceSet) reqResources.clone();
        this.colour = colour;
        this.level = level;
        this.productionPower = productionPower.clone();
        this.points = points;
    }

    /**
     * @param board the board where the card can be played
     * @param deckIndex the position of the deck where the card ca be played
     * @return true iff the card is playable on the deck card
     * @throws InvalidBoardException if the board is null
     */
    public boolean canPlay(Board board, int deckIndex) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        return board.containsResources(reqResources) && board.getTopLevel(deckIndex) == level.prev();
    }

    /**
     * play represents the act of playing the card on a card deck
     * @param board the board where the card is played
     * @param deckIndex the position of the deck where the card ca be played
     * @throws InvalidBoardException if the board is null
     */
    public void play(Board board, int deckIndex) throws InvalidBoardException {
        if(!canPlay(board, deckIndex)) {
            throw new InvalidBoardException();
        }

        board.addDevCard(this, deckIndex);
        board.addDevCardProduction(productionPower, deckIndex);
    }

    public ConcreteResourceSet getReqResources() {
        return (ConcreteResourceSet) reqResources.clone();
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

    public ProductionDetails getProductionPower() {
        return productionPower.clone();
    }

    /**
     * getPoints is a getter of card's points
     * @return points of the card
     */
    public int getPoints() {
        return points;
    }


    public DevCard clone() {
        try {
            DevCard cloneDevCard = (DevCard) super.clone();
            cloneDevCard.reqResources = getReqResources();
            cloneDevCard.productionPower = getProductionPower();
            return cloneDevCard;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
