package it.polimi.ingsw.parsing;

import com.google.gson.*;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import java.io.FileNotFoundException;

public class DevCardsParser {
    private static DevCardsParser instance;
    private JsonArray developmentCards;
    private int currentCard;
    private final Gson gson;
    private final Parser parser;
    private final DevCard[] map;

    private DevCardsParser() throws FileNotFoundException {
        gson = new Gson();
        parser = Parser.getInstance();

        developmentCards = parser.getConfig().getAsJsonArray("developmentCards");
        currentCard = 0;
        map = new DevCard[developmentCards.size()];
    }

    public static DevCardsParser getInstance() {
        if(instance == null) {
            try {
                instance = new DevCardsParser();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void setConfig(JsonObject config) {
        developmentCards = (JsonArray) config.get("developmentCards");
    }

    public DevCard getCard(int index) throws NoConfigFileException {
        if(developmentCards == null) {
            throw new NoConfigFileException();
        }

        if(index >= developmentCards.size()) {
            return null;
        }

        if(map[index] != null) {
            return map[index];
        }

        JsonObject jsonDevCard = (JsonObject) developmentCards.get(index);

        String levelString = jsonDevCard.get("level").getAsString();
        String colourString = jsonDevCard.get("colour").getAsString();

        CardLevel level = gson.fromJson(levelString, CardLevel.class);
        CardColour colour = gson.fromJson(colourString, CardColour.class);
        int points = jsonDevCard.get("points").getAsInt();
        ConcreteResourceSet reqResources = parser.parseConcreteResourceSet(jsonDevCard.get("requirements").getAsJsonArray());
        ProductionDetails productionDetails = parser.parseProductionDetails(jsonDevCard.get("productionPower").getAsJsonObject());

        return map[index] = new DevCard(reqResources, colour, level, productionDetails, points, index);
    }

    public DevCard getNextCard() throws NoConfigFileException {
        DevCard devCard = getCard(currentCard++);
        if(devCard == null) {
            currentCard = 0;
        }
        return devCard;
    }
}
