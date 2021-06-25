package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.resources.ConcreteResource;

import java.io.FileNotFoundException;

public class LeaderCardsParser {
    private static LeaderCardsParser instance;
    private final Gson gson;
    private final Parser parser;
    private final LeaderCard[] map;
    private JsonArray leaderCards;
    private int currentCard;

    private LeaderCardsParser() throws FileNotFoundException {
        gson = new Gson();
        parser = Parser.getInstance();

        leaderCards = parser.getConfig().getAsJsonArray("leaderCards");
        currentCard = 0;
        map = new LeaderCard[leaderCards.size()];
    }

    public static LeaderCardsParser getInstance() {
        if (instance == null) {
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
        if (leaderCards == null) {
            throw new NoConfigFileException();
        }

        if (index < 0 || index >= leaderCards.size()) {
            return null;
        }

        if (map[index] != null) {
            return map[index];
        }

        JsonObject jsonLeaderCard = (JsonObject) leaderCards.get(index);

        int points = jsonLeaderCard.get("points").getAsInt();
        String requirementsType = jsonLeaderCard.get("requirementsType").getAsString();

        LeaderCardRequirements requirements;

        if (requirementsType.equals("cardSet")) {
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

                return map[index] = new DiscountLeaderCard(points, requirements, resource, discount, index);
            }
            case "depot": {
                ConcreteResource resource = gson.fromJson(jsonEffect.get("resource").getAsString(), ConcreteResource.class);
                int slots = jsonEffect.get("slots").getAsInt();

                return map[index] = new DepotLeaderCard(points, requirements, resource, slots, index);
            }
            case "conversion": {
                ConcreteResource resource = gson.fromJson(jsonEffect.get("resource").getAsString(), ConcreteResource.class);

                return map[index] = new WhiteConversionLeaderCard(points, requirements, resource, index);
            }
            default: {
                ProductionDetails productionDetails = parser.parseProductionDetails(jsonEffect.getAsJsonObject("productionDetails"));

                return map[index] = new ProductionLeaderCard(points, requirements, productionDetails, index);
            }
        }
    }

    public LeaderCard getNextCard() throws NoConfigFileException {
        LeaderCard leaderCard = getCard(currentCard++);
        if (leaderCard == null) {
            currentCard = 0;
        }
        return leaderCard;
    }
}
