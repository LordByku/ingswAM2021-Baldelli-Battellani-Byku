package it.polimi.ingsw.parsing;

import com.google.gson.*;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class DevCardsParser {
    private static DevCardsParser instance;
    private final JsonArray developmentCards;
    private int currentCard;
    private final Gson gson;
    private final Parser parser;

    private DevCardsParser() throws FileNotFoundException {
        gson = new Gson();
        this.parser = Parser.getInstance();

        developmentCards = (JsonArray) Parser.getInstance().getConfig().get("developmentCards");
        currentCard = 0;
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

    public DevCard getNextCard() {
        if(currentCard >= developmentCards.size()) {
            return null;
        }

        JsonObject jsonDevCard = (JsonObject) developmentCards.get(currentCard);

        String levelString = jsonDevCard.get("level").getAsString();
        String colourString = jsonDevCard.get("colour").getAsString();

        CardLevel level = gson.fromJson(levelString, CardLevel.class);
        CardColour colour = gson.fromJson(colourString, CardColour.class);
        int points = jsonDevCard.get("points").getAsInt();
        ConcreteResourceSet reqResources = parser.parseConcreteResourceSet(jsonDevCard.get("requirements").getAsJsonArray());
        ProductionDetails productionDetails = parser.parseProductionDetails(jsonDevCard.get("productionPower").getAsJsonObject());

        return new DevCard(reqResources, colour, level, productionDetails, points, currentCard++);
    }
}
