package it.polimi.ingsw.parsing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.*;

public class Parser {
    private static Parser instance;
    private final Gson gson;

    private Parser() {
        gson = new Gson();
    }

    public static Parser getInstance() {
        if(instance == null) {
            instance = new Parser();
        }
        return instance;
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

        return new SpendableResourceSet(choiceResourceSet);
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
            } else if(resourceString.equals("CHOICE")) {
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

        return new ObtainableResourceSet(choiceResourceSet, faithPoints);
    }

    public ProductionDetails parseProductionDetails(JsonObject jsonObject) {
        SpendableResourceSet productionIn = parseSpendableResourceSet(jsonObject.get("productionIn").getAsJsonArray());
        ObtainableResourceSet productionOut = parseObtainableResourceSet(jsonObject.get("productionOut").getAsJsonArray());

        return new ProductionDetails(productionIn, productionOut);
    }
}
