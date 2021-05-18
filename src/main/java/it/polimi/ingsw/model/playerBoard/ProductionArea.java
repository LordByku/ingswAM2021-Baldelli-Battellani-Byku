package it.polimi.ingsw.model.playerBoard;

import it.polimi.ingsw.model.devCards.InvalidDevCardDeckException;
import it.polimi.ingsw.model.devCards.InvalidProductionDetailsException;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.parsing.BoardParser;

import java.util.ArrayList;

/**
 * ProductionArea is a container for all production effects in a Board
 */
public class ProductionArea {
    /**
     * developmentCardsSlots represents the number of slots of
     * development card decks, which corresponds to the number of
     * production powers that can be obtained through development cards
     */
    private final int developmentCardsSlots;



    /**
     * productions contains all the ProductionDetails in this ProductionArea
     * The first position in the array is reserved to the default production power
     * The following developmentCardsSlots positions represent the production powers
     * of the development card on top of the corresponding decks
     * Further positions are occupied by production powers provided by LeaderCards
     */
    private final ArrayList<ProductionDetails> productions;

    /**
     * The constructor initializes productions with the default production power
     */
    public ProductionArea() {
        developmentCardsSlots = BoardParser.getInstance().getDevelopmentCardsSlots();
        productions = new ArrayList<>();

        productions.add(BoardParser.getInstance().getDefaultProductionPower());
        for(int i = 0; i < developmentCardsSlots; ++i) {
            productions.add(null);
        }
    }

    public ProductionDetails getProduction(int index) {
        return productions.get(index);
    }


    public int size () {
        return productions.size();
    }
    /**
     * addDevCardProduction adds the production power corresponding to a development
     * card on top of a DevCardDeck
     * @param productionDetails The production power to add
     * @param deckIndex The DevCardDeck index
     * @throws InvalidProductionDetailsException productionDetails is null
     * @throws InvalidDevCardDeckException deckIndex is outside the range of valid decks
     */
    public void addDevCardProduction(ProductionDetails productionDetails, int deckIndex)
            throws InvalidProductionDetailsException, InvalidDevCardDeckException {
        if(productionDetails == null) {
            throw new InvalidProductionDetailsException();
        }
        if(deckIndex < 0 || deckIndex >= developmentCardsSlots) {
            throw new InvalidDevCardDeckException();
        }
        productions.set(deckIndex + 1, productionDetails.clone());
    }

    /**
     * addLeaderCardProduction adds the production power given by a LeaderCard
     * @param productionDetails The production power to add
     * @throws InvalidProductionDetailsException productionDetails is null
     */
    public void addLeaderCardProduction(ProductionDetails productionDetails) throws InvalidProductionDetailsException {
        if(productionDetails == null) {
            throw new InvalidProductionDetailsException();
        }
        productions.add(productionDetails.clone());
    }

    /**
     * getProductionDetails returns an ArrayList of the current ProductionDetails
     * available in this ProductionArea
     * @return An ArrayList containing all active ProductionDetails
     */
    public ArrayList<ProductionDetails> getProductionDetails() {
        ArrayList<ProductionDetails> result = new ArrayList<>();

        for(ProductionDetails productionDetails: productions) {
            if(productionDetails != null) {
                result.add(productionDetails.clone());
            }
        }

        return result;
    }
}
