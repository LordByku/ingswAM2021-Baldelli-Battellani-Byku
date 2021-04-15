package it.polimi.ingsw.gameZoneTest;

import it.polimi.ingsw.gameZone.InvalidMarbleMarketIndexException;
import it.polimi.ingsw.gameZone.MarbleMarket;
import it.polimi.ingsw.gameZone.marbles.MarbleColour;
import it.polimi.ingsw.leaderCards.ConversionEffect;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.resources.ChoiceResource;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.Resource;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class MarbleMarketTest {
    @Test
    public void constructorTest() {
        MarbleMarket marbleMarket = new MarbleMarket();

        int white = 0;
        int red = 0;
        int grey = 0;
        int blue = 0;
        int yellow = 0;
        int purple = 0;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 4; ++j) {
                switch(marbleMarket.getMarketColour(i, j)) {
                    case WHITE:
                        ++white;
                        break;
                    case RED:
                        ++red;
                        break;
                    case GREY:
                        ++grey;
                        break;
                    case BLUE:
                        ++blue;
                        break;
                    case YELLOW:
                        ++yellow;
                        break;
                    case PURPLE:
                        ++purple;
                        break;
                }
            }
        }

        switch(marbleMarket.getFreeMarbleColour()) {
            case WHITE:
                ++white;
                break;
            case RED:
                ++red;
                break;
            case GREY:
                ++grey;
                break;
            case BLUE:
                ++blue;
                break;
            case YELLOW:
                ++yellow;
                break;
            case PURPLE:
                ++purple;
                break;
        }

        assertEquals(4, white);
        assertEquals(1, red);
        assertEquals(2, grey);
        assertEquals(2, blue);
        assertEquals(2, yellow);
        assertEquals(2, purple);

        try {
            MarbleColour tmp = marbleMarket.getMarketColour(3, 3);
            fail();
        } catch (InvalidMarbleMarketIndexException e) {
            assertTrue(true);
        }
    }

    @Test
    public void selectRowTest() {
        MarbleMarket marbleMarket = new MarbleMarket();
        Board board = new Board();

        int expectedFaithPoints = 0;
        ConcreteResourceSet expected = new ConcreteResourceSet();

        for(int j = 0; j < 4; ++j) {
            switch (marbleMarket.getMarketColour(1, j)) {
                case WHITE:
                    break;
                case RED:
                    ++expectedFaithPoints;
                    break;
                case GREY:
                    expected.addResource(ConcreteResource.STONE);
                    break;
                case BLUE:
                    expected.addResource(ConcreteResource.SHIELD);
                    break;
                case YELLOW:
                    expected.addResource(ConcreteResource.COIN);
                    break;
                case PURPLE:
                    expected.addResource(ConcreteResource.SERVANT);
                    break;
            }
        }

        ObtainableResourceSet obtained = marbleMarket.selectRow(1, board);
        ConcreteResourceSet obtainedResources = obtained.getResourceSet().toConcrete();

        assertTrue(obtainedResources.contains(expected));
        assertTrue(expected.contains(obtainedResources));
        assertEquals(expectedFaithPoints, obtained.getFaithPoints());
    }

    @Test
    public void selectColumnTest() {
        MarbleMarket marbleMarket = new MarbleMarket();
        Board board = new Board();

        board.addConversionEffect(new ConversionEffect(ConcreteResource.COIN));

        int expectedFaithPoints = 0;
        ConcreteResourceSet expected = new ConcreteResourceSet();

        for(int i = 0; i < 3; ++i) {
            switch (marbleMarket.getMarketColour(i, 2)) {
                case WHITE:
                    expected.addResource(ConcreteResource.COIN);
                    break;
                case RED:
                    ++expectedFaithPoints;
                    break;
                case GREY:
                    expected.addResource(ConcreteResource.STONE);
                    break;
                case BLUE:
                    expected.addResource(ConcreteResource.SHIELD);
                    break;
                case YELLOW:
                    expected.addResource(ConcreteResource.COIN);
                    break;
                case PURPLE:
                    expected.addResource(ConcreteResource.SERVANT);
                    break;
            }
        }

        ObtainableResourceSet obtained = marbleMarket.selectColumn(2, board);
        ConcreteResourceSet obtainedResources = obtained.getResourceSet().toConcrete();

        assertTrue(obtainedResources.contains(expected));
        assertTrue(expected.contains(obtainedResources));
        assertEquals(expectedFaithPoints, obtained.getFaithPoints());
    }

    @Test
    public void advancedSelectTest() {
        MarbleMarket marbleMarket = new MarbleMarket();
        Board board = new Board();

        board.addConversionEffect(new ConversionEffect(ConcreteResource.COIN));
        board.addConversionEffect(new ConversionEffect(ConcreteResource.STONE));

        Random rng = new Random();

        while(marbleMarket.getFreeMarbleColour() == MarbleColour.WHITE) {
            marbleMarket.pushColumn(rng.nextInt(4));
        }

        for(int i = 0; i < 3; ++i) {
            int white = 0;
            int red = 0;
            int grey = 0;
            int blue = 0;
            int yellow = 0;
            int purple = 0;
            for(int j = 0; j < 4; ++j) {
                switch(marbleMarket.getMarketColour(i, j)) {
                    case WHITE:
                        ++white;
                        break;
                    case RED:
                        ++red;
                        break;
                    case GREY:
                        ++grey;
                        break;
                    case BLUE:
                        ++blue;
                        break;
                    case YELLOW:
                        ++yellow;
                        break;
                    case PURPLE:
                        ++purple;
                        break;
                }
            }
            if(white >= 2) {
                ObtainableResourceSet obtainableResourceSet = marbleMarket.selectRow(i, board);

                ChoiceResourceSet choiceResourceSet = obtainableResourceSet.getResourceSet();

                ArrayList<Resource> resources = choiceResourceSet.getResources();
                ArrayList<ChoiceResource> choices = new ArrayList<>();

                assertTrue(obtainableResourceSet.getFaithPoints() <= 1);
                red -= obtainableResourceSet.getFaithPoints();

                assertEquals(4, resources.size() + obtainableResourceSet.getFaithPoints());
                for(int k = 0; k < resources.size(); ++k) {
                    if(resources.get(k).isConcrete()) {
                        switch(resources.get(k).getResource()) {
                            case COIN:
                                --yellow;
                                break;
                            case STONE:
                                --grey;
                                break;
                            case SHIELD:
                                --blue;
                                break;
                            case SERVANT:
                                --purple;
                                break;
                        }
                    } else {
                        --white;
                        choices.add((ChoiceResource) resources.get(k));
                    }
                }

                assertTrue(choices.size() >= 2);

                for(int k = 0; k < choices.size(); ++k) {
                    ChoiceResource choice = choices.get(k);
                    assertFalse(choice.isConcrete());
                    assertTrue(choice.canChoose(ConcreteResource.COIN));
                    assertTrue(choice.canChoose(ConcreteResource.STONE));
                    assertFalse(choice.canChoose(ConcreteResource.SHIELD));
                    assertFalse(choice.canChoose(ConcreteResource.SERVANT));

                    if(k % 2 == 0) {
                        choice.makeChoice(ConcreteResource.COIN);
                    } else {
                        choice.makeChoice(ConcreteResource.STONE);
                    }
                }

                ConcreteResourceSet concreteResourceSet = choiceResourceSet.toConcrete();

                assertEquals(0, white);
                assertEquals(0, red);
                assertEquals(0, grey);
                assertEquals(0, blue);
                assertEquals(0, yellow);
                assertEquals(0, purple);
            }
        }

        board = new Board();
        board.addConversionEffect(new ConversionEffect(ConcreteResource.COIN));

        while(marbleMarket.getFreeMarbleColour() == MarbleColour.RED) {
            marbleMarket.pushRow(rng.nextInt(3));
        }

        for(int j = 0; j < 4; ++j) {
            int white = 0;
            int red = 0;
            int grey = 0;
            int blue = 0;
            int yellow = 0;
            int purple = 0;
            for(int i = 0; i < 3; ++i) {
                switch(marbleMarket.getMarketColour(i, j)) {
                    case WHITE:
                        ++white;
                        break;
                    case RED:
                        ++red;
                        break;
                    case GREY:
                        ++grey;
                        break;
                    case BLUE:
                        ++blue;
                        break;
                    case YELLOW:
                        ++yellow;
                        break;
                    case PURPLE:
                        ++purple;
                        break;
                }
            }
            if(red == 1) {
                ObtainableResourceSet obtainableResourceSet = marbleMarket.selectColumn(j, board);

                ConcreteResourceSet expected = new ConcreteResourceSet();
                ConcreteResourceSet obtained = obtainableResourceSet.getResourceSet().toConcrete();

                if(white + yellow > 0) {
                    expected.addResource(ConcreteResource.COIN, white + yellow);
                }
                if(grey > 0) {
                    expected.addResource(ConcreteResource.STONE, grey);
                }
                if(blue > 0) {
                    expected.addResource(ConcreteResource.SHIELD, blue);
                }
                if(purple > 0) {
                    expected.addResource(ConcreteResource.SERVANT, purple);
                }

                assertEquals(1, obtainableResourceSet.getFaithPoints());
                assertTrue(expected.contains(obtained));
                assertTrue(obtained.contains(expected));
            }
        }
    }

    @Test
    public void pushRowTest() {
        MarbleMarket marbleMarket = new MarbleMarket();
        MarbleColour[] marbleColours = new MarbleColour[5];
        MarbleColour[][] prevColours = new MarbleColour[3][4];

        for(int j = 0; j < 4; ++j) {
            marbleColours[j] = marbleMarket.getMarketColour(0, j);
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 4; ++j) {
                prevColours[i][j] = marbleMarket.getMarketColour(i, j);
            }
        }

        marbleColours[4] = marbleMarket.getFreeMarbleColour();

        marbleMarket.pushRow(0);

        assertEquals(marbleColours[0], marbleMarket.getFreeMarbleColour());

        for(int i = 0; i < 3; ++i) {
            if(i == 0) {
                for(int j = 0; j < 4; ++j) {
                    assertEquals(marbleColours[j + 1], marbleMarket.getMarketColour(0, j));
                }
            } else {
                for(int j = 0; j < 4; ++j) {
                    assertEquals(prevColours[i][j], marbleMarket.getMarketColour(i, j));
                }
            }
        }
    }

    @Test
    public void pushColumnTest() {
        MarbleMarket marbleMarket = new MarbleMarket();
        MarbleColour[] marbleColours = new MarbleColour[4];
        MarbleColour[][] prevColours = new MarbleColour[3][4];

        for(int i = 0; i < 3; ++i) {
            marbleColours[i] = marbleMarket.getMarketColour(i, 3);
        }
        marbleColours[3] = marbleMarket.getFreeMarbleColour();

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 4; ++j) {
                prevColours[i][j] = marbleMarket.getMarketColour(i, j);
            }
        }

        marbleMarket.pushColumn(3);

        assertEquals(marbleColours[0], marbleMarket.getFreeMarbleColour());

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 4; ++j) {
                if(j == 3) {
                    assertEquals(marbleColours[i + 1], marbleMarket.getMarketColour(i, j));
                } else {
                    assertEquals(prevColours[i][j], marbleMarket.getMarketColour(i, j));
                }
            }
        }
    }
}
