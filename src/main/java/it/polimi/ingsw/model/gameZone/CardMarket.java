package it.polimi.ingsw.model.gameZone;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.DevCardsParser;
import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;

import java.util.Arrays;

/**
 * This class refers to the Card Market where you can buy the devCards.
 */
public class CardMarket {
    /**
     * An ArrayList of the different decks of devCards of the devCardMarket.
     */
    private final CardMarketDeck[][] decks;

    private final String[] levelIndex = {"I", "II", "III"};
    private final String[] colourIndex = {"GREEN", "BLUE", "YELLOW", "PURPLE"};

    /**
     * The constructor creates and adds the CardMarketDecks to the ArrayList.
     */
    public CardMarket() {
        decks = new CardMarketDeck[levelIndex.length][colourIndex.length];

        JsonArray jsonDevCards = DevCardsParser.getInstance().getDevelopmentCards();
        Gson gson = new Gson();

        for(int i = 0; i < levelIndex.length; ++i) {
            for(int j = 0; j < colourIndex.length; ++j) {
                decks[i][j] = new CardMarketDeck(
                    gson.fromJson(colourIndex[j], CardColour.class),
                    gson.fromJson(levelIndex[i], CardLevel.class)
                );
            }
        }

        for(JsonElement jsonElementDevCard: jsonDevCards) {
            JsonObject jsonDevCard = (JsonObject) jsonElementDevCard;

            String levelString = jsonDevCard.get("level").getAsString();
            String colourString = jsonDevCard.get("colour").getAsString();

            CardLevel level = gson.fromJson(levelString, CardLevel.class);
            CardColour colour = gson.fromJson(colourString, CardColour.class);
            int points = jsonDevCard.get("points").getAsInt();
            ConcreteResourceSet reqResources = new ConcreteResourceSet();
            ChoiceResourceSet productionIn = new ChoiceResourceSet(), productionOut = new ChoiceResourceSet();
            int faithPointsOut = 0;

            for(JsonElement jsonElementResource: jsonDevCard.get("requirements").getAsJsonArray()) {
                JsonObject jsonResource = (JsonObject) jsonElementResource;

                ConcreteResource concreteResource = gson.fromJson(jsonResource.get("resource").getAsString(), ConcreteResource.class);
                int quantity = jsonResource.get("quantity").getAsInt();

                reqResources.addResource(concreteResource, quantity);
            }

            for(JsonElement jsonElementResource: jsonDevCard.get("productionIn").getAsJsonArray()) {
                JsonObject jsonResource = (JsonObject) jsonElementResource;

                ConcreteResource concreteResource = gson.fromJson(jsonResource.get("resource").getAsString(), ConcreteResource.class);
                int quantity = jsonResource.get("quantity").getAsInt();

                for(int i = 0; i < quantity; ++i) {
                    productionIn.addResource(concreteResource);
                }
            }


            for(JsonElement jsonElementResource: jsonDevCard.get("productionOut").getAsJsonArray()) {
                JsonObject jsonResource = (JsonObject) jsonElementResource;

                String resourceString = jsonResource.get("resource").getAsString();
                int quantity = jsonResource.get("quantity").getAsInt();

                if(resourceString.equals("FAITH POINTS")) {
                    faithPointsOut += quantity;
                } else {
                    ConcreteResource concreteResource = gson.fromJson(resourceString, ConcreteResource.class);
                    for(int i = 0; i < quantity; ++i) {
                        productionOut.addResource(concreteResource);
                    }
                }
            }

            DevCard devCard = new DevCard(reqResources, colour, level, new ProductionDetails(
                    new SpendableResourceSet(productionIn),
                    new ObtainableResourceSet(productionOut, faithPointsOut)
            ), points);


            int levelRow = Arrays.asList(levelIndex).indexOf(levelString);
            int colourColumn = Arrays.asList(colourIndex).indexOf(colourString);

            decks[levelRow][colourColumn].appendToDeck(devCard);
        }

        for(int i = 0; i < levelIndex.length; ++i) {
            for(int j = 0; j < colourIndex.length; ++j) {
                decks[i][j].shuffleDeck();
            }
        }
    }

    public DevCard top(int levelRow, int colourColumn){
        if(levelRow < 0 || levelRow >= levelIndex.length ||
           colourColumn < 0 || colourColumn >= colourIndex.length) {
            throw new InvalidCardMarketIndexException();
        }
        return decks[levelRow][colourColumn].top();
    }

    public DevCard removeTop(int levelRow, int colourColumn){
        if(levelRow < 0 || levelRow >= levelIndex.length ||
           colourColumn < 0 || colourColumn >= colourIndex.length) {
            throw new InvalidCardMarketIndexException();
        }
        DevCard card = decks[levelRow][colourColumn].removeTop();
        if(Game.getInstance().getNumberOfPlayers() == 1 && decks[levelRow][colourColumn].isEmpty())
            Game.getInstance().endGame();

        return card;
    }

    public int size(int levelRow, int colourColumn) {
        if(levelRow < 0 || levelRow >= levelIndex.length ||
           colourColumn < 0 || colourColumn >= colourIndex.length) {
            throw new InvalidCardMarketIndexException();
        }
        return decks[levelRow][colourColumn].size();
    }
}
