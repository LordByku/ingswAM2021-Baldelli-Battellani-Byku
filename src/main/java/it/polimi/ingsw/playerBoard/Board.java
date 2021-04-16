package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.*;
import it.polimi.ingsw.leaderCards.*;
import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.playerBoard.resourceLocations.*;
import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

import java.util.ArrayList;

/**
 * Board represents the board of each player
 * It is a container of the classes which represents board's elements
 */
public class Board implements ResourceLocation, Scoring, Cloneable {
    /**
     * faithTrack is the FaithTrack of this Board
     */
    private FaithTrack faithTrack;
    /**
     * productionArea stores all ProductionEffects in this Board
     */
    private ProductionArea productionArea;
    /**
     * developmentCardArea stores all development cards in this Board
     */
    private DevelopmentCardArea developmentCardArea;
    /**
     * leaderCardArea stores all LeaderCards in this Board
     */
    private LeaderCardArea leaderCardArea;
    /**
     * discountArea stores all DiscountEffects in this Board
     */
    private DiscountArea discountArea;
    /**
     * conversionEffectArea stores all ConversionEffects in this Board
     */
    private ConversionEffectArea conversionEffectArea;
    /**
     * strongBox is the StrongBox of this Board
     */
    private StrongBox strongBox;
    /**
     * warehouse is the StrongBox of this Board
     */
    private Warehouse warehouse;

    /**
     * The constructor calls the constructor of all the components
     */
    public Board(){
        faithTrack = new FaithTrack();
        productionArea = new ProductionArea();
        developmentCardArea = new DevelopmentCardArea();
        leaderCardArea = new LeaderCardArea();
        discountArea = new DiscountArea();
        conversionEffectArea = new ConversionEffectArea();
        strongBox = new StrongBox();
        warehouse = new Warehouse();
    }

    public static void setLastTurn() {}

    public ArrayList<DevCard> getCards() {
        return developmentCardArea.getCards();
    }

    public void addDevCard(DevCard devCard, int deckIndex) {
        developmentCardArea.addDevCard(devCard, deckIndex);
    }

    public void addLeaderCardProduction(ProductionDetails productionDetails) throws InvalidProductionDetailsException {
        productionArea.addLeaderCardProduction(productionDetails);
    }

    public void addDevCardProduction(ProductionDetails productionDetails, int deckIndex)
            throws InvalidDevCardDeckException, InvalidProductionDetailsException {
        productionArea.addDevCardProduction(productionDetails, deckIndex);
    }

    public void addConversionEffect(ConversionEffect conversionEffect) {
        conversionEffectArea.addConversionEffect(conversionEffect);
    }

    public void addLeaderCardDepot(LeaderCardDepot leaderCardDepot) throws InvalidLeaderCardDepotException {
        warehouse.addLeaderCardDepot(leaderCardDepot);
    }

    public void addDiscountEffect(DiscountEffect discountEffect) {
        discountArea.addDiscountEffect(discountEffect);
    }

    /**
     * containsResources checks whether a given ConcreteResourceSet
     * is contained in this Board
     * @param concreteResourceSet The ConcreteResourceSet to check
     * @return True iff this Board contains concreteResourceSet
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    @Override
    public boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        ConcreteResourceSet resources = getResources();
        return resources.contains(concreteResourceSet);
    }

    /**
     * getResources returns a copy of the resources contained in this Board
     * @return A ConcreteResourceSet representing the resources in this Board
     */
    @Override
    public ConcreteResourceSet getResources() {
        ConcreteResourceSet result = warehouse.getResources();
        result.union(strongBox.getResources());
        return result;
    }

    /**
     * addFaithPoints adds faith points to this board
     * @param points The amount of faith points to add
     */
    public void addFaithPoints(int points) {
        faithTrack.addFaithPoints(points);
    }

    /**
     * This method offers the option to add a single faith point to this board
     */
    public void addFaithPoints() {
        addFaithPoints(1);
    }

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    public Board clone() {
        try {
            return (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * addResourcesToWarehouse adds resources to a given depot
     * @param depotIndex The index of the Depot to add resources into
     * @param concreteResourceSet The ConcreteResourceSet to add
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidDepotIndexException There is no depot corresponding to depotIndex
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot be added to
     * the given depot
     */
    public void addResourcesToWarehouse(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidResourceSetException, InvalidDepotIndexException, InvalidResourceLocationOperationException {
        warehouse.addResources(depotIndex,concreteResourceSet);
    }

    /**
     * removeResourcesFromWarehouse removes resources from a given depot
     * @param depotIndex The index of the Depot to remove resources from
     * @param concreteResourceSet The ConcreteResourceSet to remove
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidDepotIndexException There is no depot corresponding to depotIndex
     * @throws InvalidResourceLocationOperationException concreteResourceSet cannot be
     * removed from the given depot
     */
    public void removeResourcesFromWarehouse(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidResourceSetException, InvalidDepotIndexException, InvalidResourceLocationOperationException {
        warehouse.removeResources(depotIndex,concreteResourceSet);
    }

    /**
     * addResourcesToStrongBox adds resources to the StrongBox
     * @param concreteResourceSet The ConcreteResourceSet to add
     * @throws InvalidResourceSetException concreteResourceSet is null
     */
    public void addResourcesToStrongbox(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        strongBox.addResources(concreteResourceSet);
    }

    /**
     * Removes resources from Strongbox
     * @param concreteResourceSet The set of Resources to be removed.
     * @throws InvalidResourceSetException concreteResourceSet is null
     * @throws InvalidResourceLocationOperationException The strongbox doesn't contain the resourceSet to be removed.
     */
    public void removeResourcesFromStrongbox(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException, InvalidResourceLocationOperationException {
        strongBox.removeResources(concreteResourceSet);
    }

    /**
     * Adds a leaderCard to the leaderCardsArea.
     * @param leaderCard The leaderCard to be added to the leaderCardArea
     */
    public void addLeaderCard(LeaderCard leaderCard){
        leaderCardArea.addLeaderCard(leaderCard);
    }

    /**
     * Remove leaderCard from the ArrayList.
     * @param leaderCard The leaderCard to be removed.
     */
    public void removeLeaderCard(LeaderCard leaderCard){
        leaderCardArea.removeLeaderCard(leaderCard);
    }

    public ChoiceSet getConversionEffects() {
        return conversionEffectArea.getConversionEffects();
    }

    public ConcreteResourceSet applyDiscount(ConcreteResourceSet concreteResourceSet) {
        return discountArea.applyDiscount((ConcreteResourceSet) concreteResourceSet.clone());
    }

    /**
     * getPoints returns the total points obtained by this board, not including
     * points awarded by resources
     * @return The points obtained by this board
     */
    @Override
    public int getPoints() {
        return faithTrack.getPoints() + developmentCardArea.getPoints() + leaderCardArea.getPoints();
    }

    public CardLevel getTopLevel(int deckIndex) {
        return developmentCardArea.getTopLevel(deckIndex);
    }

    public int getFaithPoints() {
        return faithTrack.getMarkerPosition();
    }
}
