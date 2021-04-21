package it.polimi.ingsw.model.gameZoneTest;

import it.polimi.ingsw.model.gameZone.CardMarket;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardMarketTest {
    @Test
    public void constructorTest() {
        CardMarket cardMarket = new CardMarket();

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 4; ++j) {
                assertEquals(4, cardMarket.size(i, j));
            }
        }
    }
}
