package it.polimi.ingsw.playerBoard;

import it.polimi.ingsw.devCards.InvalidDevCardDeckException;
import it.polimi.ingsw.devCards.InvalidProductionDetailsException;
import it.polimi.ingsw.devCards.ProductionDetails;
import it.polimi.ingsw.resources.ChoiceResource;
import it.polimi.ingsw.resources.FullChoiceSet;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.resources.resourceSets.SpendableResourceSet;

import java.util.ArrayList;

/**
 * ProductionArea is a container for all production effects in a Board
 */
public class ProductionArea {
    /**
     * productions contains all the ProductionDetails in this ProductionArea
     * The first position in the array is reserved to the default production power
     * The following three positions represent the production powers of the
     * development card on top of the corresponding decks
     * Further positions are occupied by production powers provided by LeaderCards
     */
    private ArrayList<ProductionDetails> productions;

    /**
     * The constructor initializes productions with the default production power
     */
    public ProductionArea() {
        productions = new ArrayList<>();

        ChoiceResourceSet inputChoiceResourceSet = new ChoiceResourceSet();
        inputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        inputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        ChoiceResourceSet outputChoiceResourceSet = new ChoiceResourceSet();
        outputChoiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));

        productions.add(new ProductionDetails(new SpendableResourceSet(inputChoiceResourceSet),
                                              new ObtainableResourceSet(outputChoiceResourceSet)));
        productions.add(null);
        productions.add(null);
        productions.add(null);
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
        if(deckIndex < 0 || deckIndex >= 3) {
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
