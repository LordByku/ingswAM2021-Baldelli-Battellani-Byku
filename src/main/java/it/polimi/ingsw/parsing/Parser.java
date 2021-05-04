package it.polimi.ingsw.parsing;

import com.google.gson.*;
import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Parser {
    private static Parser instance;
    private final Gson gson;
    private JsonObject config;
    private static final String path = "src/resources/config.json";

    private Parser() throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(Parser.path);
        config = (JsonObject) parser.parse(reader);

        gson = new Gson();
    }

    public static Parser getInstance() {
        if(instance == null) {
            try {
                instance = new Parser();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public JsonObject getConfig() {
        return config;
    }

    public void setConfig(JsonObject config) {
        this.config = config;
        BoardParser.getInstance().setConfig(config);
        DevCardsParser.getInstance().setConfig(config);
        LeaderCardsParser.getInstance().setConfig(config);
        VRSParser.getInstance().setConfig(config);
    }

    public ConcreteResourceSet parseConcreteResourceSet(JsonArray jsonArray)
            throws InvalidResourceException, InvalidQuantityException {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        for(JsonElement jsonElementResource: jsonArray) {
            JsonObject jsonResource = (JsonObject) jsonElementResource;

            ConcreteResource concreteResource = gson.fromJson(jsonResource.get("resource").getAsString(), ConcreteResource.class);
            int quantity = jsonResource.get("quantity").getAsInt();

            concreteResourceSet.addResource(concreteResource, quantity);
        }

        return concreteResourceSet;
    }

    public CardTypeSet parseCardTypeSet(JsonArray jsonArray) {
        CardTypeSet cardTypeSet = new CardTypeSet();

        for(JsonElement jsonElementCardType: jsonArray) {
            JsonObject jsonCardType = (JsonObject) jsonElementCardType;

            CardType cardType = new CardType(gson.fromJson(jsonCardType.get("colour").getAsString(), CardColour.class));
            for(JsonElement jsonElementLevel: jsonCardType.getAsJsonArray("levelSet")) {
                cardType.addLevel(gson.fromJson(jsonElementLevel.getAsString(), CardLevel.class));
            }

            int quantity = jsonCardType.get("quantity").getAsInt();

            cardTypeSet.add(cardType, quantity);
        }

        return cardTypeSet;
    }

    public SpendableResourceSet parseSpendableResourceSet(JsonArray jsonArray) throws InvalidResourceException {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        for(JsonElement jsonElementResource: jsonArray) {
            JsonObject jsonResource = (JsonObject) jsonElementResource;

            String resourceString = jsonResource.get("resource").getAsString();
            int quantity = jsonResource.get("quantity").getAsInt();

            addResources(choiceResourceSet, resourceString, quantity);
        }

        return new SpendableResourceSet(choiceResourceSet);
    }

    private void addResources(ChoiceResourceSet choiceResourceSet, String resourceString, int quantity) {
        if(resourceString.equals("CHOICE")) {
            for(int i = 0; i < quantity; ++i) {
                choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
            }
        } else {
            ConcreteResource concreteResource = gson.fromJson(resourceString, ConcreteResource.class);
            for(int i = 0; i < quantity; ++i) {
                choiceResourceSet.addResource(concreteResource);
            }
        }
    }

    public ObtainableResourceSet parseObtainableResourceSet(JsonArray jsonArray) {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        int faithPoints = 0;

        for(JsonElement jsonElementResource: jsonArray) {
            JsonObject jsonResource = (JsonObject) jsonElementResource;

            String resourceString = jsonResource.get("resource").getAsString();
            int quantity = jsonResource.get("quantity").getAsInt();

            if(resourceString.equals("FAITH POINTS")) {
                faithPoints += quantity;
            } else {
                addResources(choiceResourceSet, resourceString, quantity);
            }
        }

        return new ObtainableResourceSet(choiceResourceSet, faithPoints);
    }

    public ProductionDetails parseProductionDetails(JsonObject jsonObject) {
        SpendableResourceSet productionIn = parseSpendableResourceSet(jsonObject.get("productionIn").getAsJsonArray());
        ObtainableResourceSet productionOut = parseObtainableResourceSet(jsonObject.get("productionOut").getAsJsonArray());

        return new ProductionDetails(productionIn, productionOut);
    }
}
