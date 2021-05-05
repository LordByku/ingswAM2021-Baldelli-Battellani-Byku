package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.resources.ConcreteResource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class LeaderCardsParser {
    private static LeaderCardsParser instance;
    private JsonArray leaderCards;
    private int currentCard;
    private final Gson gson;
    private final Parser parser;
    private final LeaderCard[] map;

    private LeaderCardsParser() throws FileNotFoundException {
        gson = new Gson();
        parser = Parser.getInstance();

        leaderCards = parser.getConfig().getAsJsonArray("leaderCards");
        currentCard = 0;
        map = new LeaderCard[leaderCards.size()];
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

    public void setConfig(JsonObject config) {
        leaderCards = (JsonArray) config.get("leaderCards");
    }

    public LeaderCard getCard(int index) throws NoConfigFileException {
        if(leaderCards == null) {
            throw new NoConfigFileException();
        }

        if(index < 0 || index >= leaderCards.size()) {
            return null;
        }

        if(map[index] != null) {
            return map[index];
        }

        JsonObject jsonLeaderCard = (JsonObject) leaderCards.get(index);

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

        LeaderCard leaderCard;

        switch (effectType) {
            case "discount": {
                ConcreteResource resource = gson.fromJson(jsonEffect.get("resource").getAsString(), ConcreteResource.class);
                int discount = jsonEffect.get("discount").getAsInt();

                leaderCard = new DiscountLeaderCard(points, requirements, resource, discount, index);
                break;
            }
            case "depot": {
                ConcreteResource resource = gson.fromJson(jsonEffect.get("resource").getAsString(), ConcreteResource.class);
                int slots = jsonEffect.get("slots").getAsInt();

                leaderCard = new DepotLeaderCard(points, requirements, resource, slots, index);
                break;
            }
            case "conversion": {
                ConcreteResource resource = gson.fromJson(jsonEffect.get("resource").getAsString(), ConcreteResource.class);

                leaderCard = new WhiteConversionLeaderCard(points, requirements, resource, index);
                break;
            }
            default: {
                ProductionDetails productionDetails = parser.parseProductionDetails(jsonEffect.getAsJsonObject("productionDetails"));

                leaderCard = new ProductionLeaderCard(points, requirements, productionDetails, index);
                break;
            }
        }

        return map[index] = leaderCard;
    }

    public LeaderCard nextCard() throws NoConfigFileException {
        return getCard(currentCard++);
    }
}
