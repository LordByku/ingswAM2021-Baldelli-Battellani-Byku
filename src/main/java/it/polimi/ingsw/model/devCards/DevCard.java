package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.InvalidBoardException;
import it.polimi.ingsw.model.playerBoard.Scoring;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;
import it.polimi.ingsw.view.cli.BackGroundColor;
import it.polimi.ingsw.view.cli.Strings;

import java.util.HashSet;

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
    private final CardColour colour;
    /**
     * level represents the card's level
     */
    private final CardLevel level;
    /**
     * productionPower represents the production details of the card
     */
    private ProductionDetails productionPower;
    /**
     * points represents the card's points
     */
    private final int points;
    /**
     * id is the unique id of this card
     */
    private final int id;
    /**
     * usedIds is a container for the ids already used for development cards
     */
    private final static HashSet<Integer> usedIds = new HashSet<>();

    protected static int width = 0;

    /**
     * The constructor
     * @param reqResources set of resources required to buy the card
     * @param colour the colour to be set
     * @param level The level of the card
     * @param productionPower The Production effect of the card
     * @param points The points awarded by the card
     * @throws InvalidCardColourException colour is null
     * @throws InvalidCardLevelException level is null
     * @throws InvalidResourceSetException reqResources is null
     * @throws InvalidProductionDetailsException productionPower is null
     */
    public DevCard(ConcreteResourceSet reqResources, CardColour colour, CardLevel level, ProductionDetails productionPower, int points, int id)
            throws InvalidCardColourException, InvalidCardLevelException, InvalidResourceSetException, InvalidProductionDetailsException, InvalidIdException {
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
        if(id < 0 || usedIds.contains(id)) {
            throw new InvalidIdException();
        }
        this.reqResources = (ConcreteResourceSet) reqResources.clone();
        this.colour = colour;
        this.level = level;
        this.productionPower = productionPower.clone();
        this.points = points;
        this.id = id;
        usedIds.add(id);
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
        return board.containsResources(reqResources) && board.getDevelopmentCardArea().getTopLevel(deckIndex) == level.prev();
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

        board.getDevelopmentCardArea().addDevCard(this, deckIndex);
        board.getProductionArea().addDevCardProduction(productionPower, deckIndex);
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

    public int getId() {
        return id;
    }

    public void addCLISupport() {
        width = Math.max(width, Strings.getGraphemesCount(reqResources.getCLIString()) + 2); //
        width = Math.max(width, Strings.getGraphemesCount(productionPower.getCLIString()));
    }

    public String getCLIString(){
        String reqResources = this.reqResources.getCLIString();
        String first;
        String second;
        if(this.level==CardLevel.I){
            first = "" ;
            second = ".";
        }
        else if(this.level==CardLevel.II){
            first = "" ;
            second = ":";
        }
        else{
            first = ".";
            second = ":";
        }
        String colourFirst = this.colour.getColour().escape() + first + BackGroundColor.RESET;
        String colourSecond = this.colour.getColour().escape() + second + BackGroundColor.RESET;
        int reqResourcesLength = Strings.getGraphemesCount(reqResources);
        StringBuilder result = new StringBuilder(" ");
        for(int i = 0; i < width; ++i) {
            result.append("_");
        }
        Strings.buildCenteredRow(result,colourFirst,colourSecond, reqResources,reqResourcesLength,width);
        Strings.buildCenteredRow(result,colourSecond,colourSecond, "",0,width);
        Strings.newEmptyLine(result,width);

        String productionPower = this.productionPower.getCLIString();
        int productionPowerLength = Strings.getGraphemesCount(productionPower);

        Strings.buildCenteredRow(result,"","", productionPower,productionPowerLength,width);
        Strings.newEmptyLine(result,width);

        String points = "(" + this.points + ")";
        int pointsLength = Strings.getGraphemesCount(points);
        Strings.buildCenteredRow(result,"","", points, pointsLength, width);

        result.append("|");
        for(int i = 0; i < width; ++i) {
            result.append("_");
        }
        result.append("|");
        return result.toString();
    }
}
