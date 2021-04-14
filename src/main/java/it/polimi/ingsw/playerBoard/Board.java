package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.CardTypeSet;
import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.leaderCards.*;
import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.playerBoard.resourceLocations.*;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.InvalidResourceSetException;

/**
 * Board represents the board of each player
 * It is a container of the classes which represents board's elements
 */
public class Board implements ResourceLocation, Scoring, Cloneable {
    private FaithTrack faithTrack;
    private ProductionArea productionArea;
    private DevelopmentCardArea developmentCardArea;
    private LeaderCardArea leaderCardArea;
    private DiscountArea discountArea;
    private ConversionEffectArea conversionEffectArea;
    private StrongBox strongBox;
    private Warehouse warehouse;

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

    public void hasCardTypeSet(CardTypeSet cardSet) {}

    public void addProduction(ProductionDetails productionDetails) {
        productionArea.addProduction(productionDetails);
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

    @Override
    public boolean containsResources(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        ConcreteResourceSet resources = getResources();
        return resources.contains(concreteResourceSet);
    }

    @Override
    public ConcreteResourceSet getResources() {
        ConcreteResourceSet result = warehouse.getResources();
        result.union(strongBox.getResources());
        return result;
    }

    public void addFaithPoints() {
        faithTrack.addFaithPoints();
    }

    /**
     * clone returns a copy of the object
     * @return A copy of the object
     */
    public Board clone() {
        try {
            Board cloneBoard = (Board) super.clone();
            return cloneBoard;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addResourcesToWarehouse(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidResourceSetException, InvalidDepotIndexException, InvalidResourceLocationOperationException {
        warehouse.addResources(depotIndex,concreteResourceSet);
    }

    public void removeResourcesFromWarehouse(int depotIndex, ConcreteResourceSet concreteResourceSet)
            throws InvalidResourceSetException, InvalidDepotIndexException, InvalidResourceLocationOperationException {
        warehouse.removeResources(depotIndex,concreteResourceSet);
    }

    public void addResourcesToStrongbox(ConcreteResourceSet concreteResourceSet) throws InvalidResourceSetException {
        strongBox.addResources(concreteResourceSet);
    }

    /**
     * Removes resources from Strongbox
     * @param concreteResourceSet The set of Resources to be removed.
     * @throws InvalidResourceSetException concreteResourceSet is null or .
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
}
