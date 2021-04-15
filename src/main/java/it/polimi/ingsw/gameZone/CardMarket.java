package it.polimi.ingsw.gameZone;

import it.polimi.ingsw.devCards.*;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

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

        //TODO: Load DevCards from JSON

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
