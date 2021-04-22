package it.polimi.ingsw.model.gameZoneTest;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.DevCard;
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

                for(int k = 0; k < cardMarket.size(i, j); ++k) {
                    DevCard devCard = cardMarket.removeTop(i, j);

                    switch(i) {
                        case 0:
                            assertEquals(CardLevel.I, devCard.getLevel());
                            break;
                        case 1:
                            assertEquals(CardLevel.II, devCard.getLevel());
                            break;
                        case 2:
                            assertEquals(CardLevel.III, devCard.getLevel());
                            break;
                    }
                    switch(j) {
                        case 0:
                            assertEquals(CardColour.GREEN, devCard.getColour());
                            break;
                        case 1:
                            assertEquals(CardColour.BLUE, devCard.getColour());
                            break;
                        case 2:
                            assertEquals(CardColour.YELLOW, devCard.getColour());
                            break;
                        case 3:
                            assertEquals(CardColour.PURPLE, devCard.getColour());
                            break;
                    }
                }
            }
        }
    }
}
