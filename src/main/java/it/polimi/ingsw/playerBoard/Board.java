package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.CardTypeSet;
import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.leaderCards.ConversionEffect;
import it.polimi.ingsw.leaderCards.DiscountEffect;
import it.polimi.ingsw.leaderCards.InvalidLeaderCardDepotException;
import it.polimi.ingsw.leaderCards.LeaderCardDepot;
import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.playerBoard.resourceLocations.ResourceLocation;
import it.polimi.ingsw.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.resources.InvalidResourceException;
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
        try {
            result.union(strongBox.getResources());
        } catch (InvalidResourceSetException e) {}
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
}
