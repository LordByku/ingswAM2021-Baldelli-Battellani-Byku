package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.leaderCards.ConversionEffect;
import it.polimi.ingsw.leaderCards.DiscountEffect;
import it.polimi.ingsw.leaderCards.LeaderCardDepot;
import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.playerBoard.resourceLocations.ResourceLocation;
import it.polimi.ingsw.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.playerBoard.resourceLocations.Warehouse;
import it.polimi.ingsw.resources.ConcreteResource;

public class Board implements ResourceLocation, Scoring {
    private FaithTrack faithTrack;
    private ProductionArea productionArea;
    private DevelopmentCardArea developmentCardArea;
    private LeaderCardArea leaderCardArea;
    private DiscountArea discountArea;
    private ConversionEffectArea conversionEffectArea;
    private StrongBox strongBox;
    private Warehouse warehouse;

    public void addProduction(ProductionDetails productionDetails) {
        productionArea.addProduction(productionDetails);
    }

    public void addConversionEffect(ConversionEffect conversionEffect) {
        conversionEffectArea.addConversionEffect(conversionEffect);
    }

    public void addLeaderCardDepot(LeaderCardDepot leaderCardDepot) {
        warehouse.addLeaderCardDepot(leaderCardDepot);
    }

    public void addDiscountEffect(DiscountEffect discountEffect) {
        discountArea.addDiscountEffect(discountEffect);
    }
}
