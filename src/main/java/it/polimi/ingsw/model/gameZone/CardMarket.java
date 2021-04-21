package it.polimi.ingsw.model.gameZone;

import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;

import java.util.ArrayList;

/**
 * This class refers to the Card Market where you can buy the devCards.
 */
public class CardMarket {

    /**
     * An ArrayList of the different decks of devCards of the devCardMarket.
     */
    private ArrayList<CardMarketDeck> decks;

    /**
     * The constructor creates and adds the CardMarketDecks to the ArrayList.
     */
    public CardMarket(){
        CardMarketDeck deckG1 = new CardMarketDeck(CardColour.GREEN, CardLevel.I);
        CardMarketDeck deckB1 = new CardMarketDeck(CardColour.BLUE, CardLevel.I);
        CardMarketDeck deckY1 = new CardMarketDeck(CardColour.YELLOW, CardLevel.I);
        CardMarketDeck deckP1 = new CardMarketDeck(CardColour.PURPLE, CardLevel.I);
        CardMarketDeck deckG2 = new CardMarketDeck(CardColour.GREEN, CardLevel.II);
        CardMarketDeck deckB2 = new CardMarketDeck(CardColour.BLUE, CardLevel.II);
        CardMarketDeck deckY2 = new CardMarketDeck(CardColour.YELLOW, CardLevel.II);
        CardMarketDeck deckP2 = new CardMarketDeck(CardColour.PURPLE, CardLevel.II);
        CardMarketDeck deckG3 = new CardMarketDeck(CardColour.GREEN, CardLevel.III);
        CardMarketDeck deckB3 = new CardMarketDeck(CardColour.BLUE, CardLevel.III);
        CardMarketDeck deckY3 = new CardMarketDeck(CardColour.YELLOW, CardLevel.III);
        CardMarketDeck deckP3 = new CardMarketDeck(CardColour.PURPLE, CardLevel.III);

        //SOME CARDS
        //GREEN level 1, 1VP, 1 STONE, (COIN --> SERVANT + 1FP)
        ChoiceResourceSet obt = new ChoiceResourceSet();
        ChoiceResourceSet spn = new ChoiceResourceSet();

        spn.addResource(ConcreteResource.COIN);
        obt.addResource(ConcreteResource.SERVANT);

        SpendableResourceSet spendG1 = new SpendableResourceSet(spn);
        ObtainableResourceSet obtG1 = new ObtainableResourceSet(obt,1);

        ProductionDetails prodG1 = new ProductionDetails(spendG1,obtG1);
        ConcreteResourceSet reqG1 = new ConcreteResourceSet();
        reqG1.addResource(ConcreteResource.STONE);
        DevCard green1 = new DevCard(reqG1, CardColour.GREEN,CardLevel.I,prodG1,1);
        deckG1.appendToDeck(green1);

        //BLUE level 2, 1VP, 1 COIN, (CHOICE --> CHOICE + 1FP)
        ChoiceResourceSet obt2 = new ChoiceResourceSet();
        ChoiceResourceSet spn2 = new ChoiceResourceSet();

        ChoiceSet set = new ChoiceSet();
        set.addChoice(ConcreteResource.COIN);
        set.addChoice(ConcreteResource.STONE);
        set.addChoice(ConcreteResource.SERVANT);
        set.addChoice(ConcreteResource.SHIELD);
        ChoiceResource choice = new ChoiceResource(set);

        spn2.addResource(choice);
        obt2.addResource(choice);

        SpendableResourceSet spendB2 = new SpendableResourceSet(spn2);
        ObtainableResourceSet obtB2 = new ObtainableResourceSet(obt2,1);

        ProductionDetails prodB2 = new ProductionDetails(spendB2,obtB2);
        ConcreteResourceSet reqB2 = new ConcreteResourceSet();
        reqG1.addResource(ConcreteResource.COIN);
        DevCard blue2 = new DevCard(reqB2, CardColour.BLUE,CardLevel.II,prodB2,1);
        deckB2.appendToDeck(blue2);


        decks.add(deckG1);
        decks.add(deckB1);
        decks.add(deckY1);
        decks.add(deckP1);
        decks.add(deckG2);
        decks.add(deckB2);
        decks.add(deckY2);
        decks.add(deckP2);
        decks.add(deckG3);
        decks.add(deckB3);
        decks.add(deckY3);
        decks.add(deckP3);

    }

    /**
     * @param cardMarketDeck The Deck of which we want to know the card a the top.
     * @return The card at the top of the cardMarketDeck.
     */
    public DevCard top(CardMarketDeck cardMarketDeck){
        return cardMarketDeck.top();
    }

    /**
     * Removes the card a the top of the cardMarketDeck
     * @param cardMarketDeck The chosen deck from which remove the card at the top.
     * @return The card removed.
     */
    public DevCard removeTop(CardMarketDeck cardMarketDeck){
        return cardMarketDeck.removeTop();
    }


}
