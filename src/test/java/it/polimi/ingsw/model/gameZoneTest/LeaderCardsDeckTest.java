package it.polimi.ingsw.model.gameZoneTest;

import it.polimi.ingsw.model.gameZone.LeaderCardsDeck;
import it.polimi.ingsw.model.leaderCards.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeaderCardsDeckTest {
    @Test
    public void constructorTest() {
        LeaderCardsDeck leaderCardsDeck = new LeaderCardsDeck();

        int production = 0;
        int depot = 0;
        int conversion = 0;
        int discount = 0;

        assertEquals(16, leaderCardsDeck.size());

        for(int i = 0; i < 16; ++i) {
            LeaderCard leaderCard = leaderCardsDeck.removeTop();

            if(leaderCard instanceof ProductionLeaderCard) {
                ++production;
            }

            if(leaderCard instanceof DepotLeaderCard) {
                ++depot;
            }

            if(leaderCard instanceof WhiteConversionLeaderCard) {
                ++conversion;
            }

            if(leaderCard instanceof DiscountLeaderCard) {
                ++discount;
            }
        }

        assertEquals(4, production);
        assertEquals(4, depot);
        assertEquals(4, conversion);
        assertEquals(4, discount);
    }
}
