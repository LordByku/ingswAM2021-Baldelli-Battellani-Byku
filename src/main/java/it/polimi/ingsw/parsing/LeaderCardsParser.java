package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.devCards.CardTypeSet;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class LeaderCardsParser {
    private static LeaderCardsParser instance;
    private static final String path = "src/resources/leaderCards.json";
    private final JsonArray leaderCards;
    private int currentCard;
    private final Gson gson;
    private final Parser parser;

    private LeaderCardsParser() throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(path);
        JsonObject obj = (JsonObject) parser.parse(reader);

        gson = new Gson();
        this.parser = Parser.getInstance();

        leaderCards = (JsonArray) obj.get("leaderCards");
        currentCard = 0;
    }

    public static LeaderCardsParser getInstance() {
        if(instance == null) {
            try {
                instance = new LeaderCardsParser();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public LeaderCard nextCard() {
        if(currentCard >= leaderCards.size()) {
            return null;
        }

        JsonObject jsonLeaderCard = (JsonObject) leaderCards.get(currentCard++);

        int points = jsonLeaderCard.get("points").getAsInt();
        String requirementsType = jsonLeaderCard.get("requirementsType").getAsString();

        LeaderCardRequirements requirements;

        if(requirementsType.equals("cardSet")) {
            requirements = parser.parseCardTypeSet(jsonLeaderCard.getAsJsonArray("requirements"));
        } else {
            requirements = parser.parseConcreteResourceSet(jsonLeaderCard.getAsJsonArray("requirements"));
        }

        JsonObject jsonEffect = jsonLeaderCard.getAsJsonObject("effect");

        String effectType = jsonEffect.get("effectType").getAsString();

        switch (effectType) {
            case "discount": {
                ConcreteResource resource = gson.fromJson(jsonEffect.get("resource").getAsString(), ConcreteResource.class);
                int discount = jsonEffect.get("discount").getAsInt();

                return new DiscountLeaderCard(points, requirements, resource, discount);
            }
            case "depot": {
                ConcreteResource resource = gson.fromJson(jsonEffect.get("resource").getAsString(), ConcreteResource.class);
                int slots = jsonEffect.get("slots").getAsInt();

                return new DepotLeaderCard(points, requirements, resource, slots);
            }
            case "conversion": {
                ConcreteResource resource = gson.fromJson(jsonEffect.get("resource").getAsString(), ConcreteResource.class);

                return new WhiteConversionLeaderCard(points, requirements, resource);
            }
            default:
                ProductionDetails productionDetails = parser.parseProductionDetails(jsonEffect.getAsJsonObject("productionDetails"));

                return new ProductionLeaderCard(points, requirements, productionDetails);
        }
    }
}
