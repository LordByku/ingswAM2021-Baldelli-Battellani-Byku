package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.playerBoard.resourceLocations.ResourceLocation;
import it.polimi.ingsw.playerBoard.resourceLocations.StrongBox;
import it.polimi.ingsw.playerBoard.resourceLocations.Warehouse;

public class Board implements ResourceLocation, Scoring {
    private FaithTrack faithTrack;
    private ProductionArea productionArea;
    private DevelopmentCardArea developmentCardArea;
    private LeaderCardArea leaderCardArea;
    private DiscountArea discountArea;
    private ConversionEffectArea conversionEffectArea;
    private StrongBox strongBox;
    private Warehouse warehouse;
}
