package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.playerBoard.resourceLocations.ResourceLocation;
import it.polimi.ingsw.model.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.model.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.InvalidResourceSetException;

/**
 * Board represents the board of each player
 * It is a container of the classes which represents board's elements
 */
public class Board implements ResourceLocation, Scoring, Cloneable {
    /**
     * faithTrack is the FaithTrack of this Board
     */
    private final FaithTrack faithTrack;
    /**
     * productionArea stores all ProductionEffects in this Board
     */
    private final ProductionArea productionArea;
    /**
     * developmentCardArea stores all development cards in this Board
     */
    private final DevelopmentCardArea developmentCardArea;
    /**
     * leaderCardArea stores all LeaderCards in this Board
     */
    private final LeaderCardArea leaderCardArea;
    /**
     * discountArea stores all DiscountEffects in this Board
     */
    private final DiscountArea discountArea;
    /**
     * conversionEffectArea stores all ConversionEffects in this Board
     */
    private final ConversionEffectArea conversionEffectArea;
    /**
     * strongBox is the StrongBox of this Board
     */
    private final StrongBox strongBox;
    /**
     * warehouse is the StrongBox of this Board
     */
    private final Warehouse warehouse;

    /**
     * The constructor calls the constructor of all the components
     */
    public Board() {
        faithTrack = FaithTrack.builder();
        productionArea = new ProductionArea();
        developmentCardArea = new DevelopmentCardArea();
        leaderCardArea = new LeaderCardArea();
        discountArea = new DiscountArea();
        conversionEffectArea = new ConversionEffectArea();
        strongBox = new StrongBox();
        warehouse = new Warehouse();
    }

    public static void setLastTurn() {
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public ProductionArea getProductionArea() {
        return productionArea;
    }

    public DevelopmentCardArea getDevelopmentCardArea() {
        return developmentCardArea;
    }

    public LeaderCardArea getLeaderCardArea() {
        return leaderCardArea;
    }

    public DiscountArea getDiscountArea() {
        return discountArea;
    }

    public ConversionEffectArea getConversionEffectArea() {
        return conversionEffectArea;
    }

    public StrongBox getStrongBox() {
        return strongBox;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * containsResources checks whether a given ConcreteResourceSet
     * is contained in this Board
     *
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
     *
     * @return A ConcreteResourceSet representing the resources in this Board
     */
    @Override
    public ConcreteResourceSet getResources() {
        ConcreteResourceSet result = warehouse.getResources();
        result.union(strongBox.getResources());
        return result;
    }

    /**
     * clone returns a copy of the object
     *
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
     * getPoints returns the total points obtained by this board, not including
     * points awarded by resources
     *
     * @return The points obtained by this board
     */
    @Override
    public int getPoints() {
        return faithTrack.getPoints() + developmentCardArea.getPoints() + leaderCardArea.getPoints();
    }
}
