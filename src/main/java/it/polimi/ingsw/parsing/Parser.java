package it.polimi.ingsw.parsing;

import com.google.gson.*;
import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Parser {
    private static final String filename = "config";
    private static Parser instance;
    private final Gson gson;
    private JsonObject config;

    private Parser() throws IOException {
        JsonParser parser = new JsonParser();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(filename + ".json");
        config = (JsonObject) parser.parse(new InputStreamReader(resource.openStream()));

        gson = new Gson();
    }

    public static Parser getInstance() {
        if (instance == null) {
            try {
                instance = new Parser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public JsonObject getConfig() {
        return config;
    }

    public void setConfig(JsonObject config) {
        BoardParser.getInstance().setConfig(config);
        DevCardsParser.getInstance().setConfig(config);
        LeaderCardsParser.getInstance().setConfig(config);
        VRSParser.getInstance().setConfig(config);
        InitGameParser.getInstance().setConfig(config);
        if (!config.equals(this.config)) {
            this.config = config;
            Game.getInstance().refreshModel();
        }
    }

    public ConcreteResourceSet parseConcreteResourceSet(JsonArray jsonArray)
            throws InvalidResourceException, InvalidQuantityException {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        for (JsonElement jsonElementResource : jsonArray) {
            JsonObject jsonResource = (JsonObject) jsonElementResource;

            ConcreteResource concreteResource = gson.fromJson(jsonResource.get("resource").getAsString(), ConcreteResource.class);
            int quantity = jsonResource.get("quantity").getAsInt();

            concreteResourceSet.addResource(concreteResource, quantity);
        }

        return concreteResourceSet;
    }

    public CardTypeSet parseCardTypeSet(JsonArray jsonArray) {
        CardTypeSet cardTypeSet = new CardTypeSet();

        for (JsonElement jsonElementCardType : jsonArray) {
            CardTypeDetails cardTypeDetails = gson.fromJson(jsonElementCardType, CardTypeDetails.class);
            cardTypeSet.add(cardTypeDetails);
        }

        return cardTypeSet;
    }

    public SpendableResourceSet parseSpendableResourceSet(JsonArray jsonArray) throws InvalidResourceException {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        for (JsonElement jsonElementResource : jsonArray) {
            JsonObject jsonResource = (JsonObject) jsonElementResource;

            String resourceString = jsonResource.get("resource").getAsString();
            int quantity = jsonResource.get("quantity").getAsInt();

            addResources(choiceResourceSet, resourceString, quantity);
        }

        return new SpendableResourceSet(choiceResourceSet);
    }

    private void addResources(ChoiceResourceSet choiceResourceSet, String resourceString, int quantity) {
        if (resourceString.equals("CHOICE")) {
            for (int i = 0; i < quantity; ++i) {
                choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
            }
        } else {
            ConcreteResource concreteResource = gson.fromJson(resourceString, ConcreteResource.class);
            for (int i = 0; i < quantity; ++i) {
                choiceResourceSet.addResource(concreteResource);
            }
        }
    }

    public ObtainableResourceSet parseObtainableResourceSet(JsonArray jsonArray) {
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        int faithPoints = 0;

        for (JsonElement jsonElementResource : jsonArray) {
            JsonObject jsonResource = (JsonObject) jsonElementResource;

            String resourceString = jsonResource.get("resource").getAsString();
            int quantity = jsonResource.get("quantity").getAsInt();

            if (resourceString.equals("FAITHPOINTS")) {
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

    public boolean validateConfig(JsonObject config) {
        try {
            BoardParser.getInstance().setConfig(config);
            DevCardsParser.getInstance().setConfig(config);
            LeaderCardsParser.getInstance().setConfig(config);
            VRSParser.getInstance().setConfig(config);
            InitGameParser.getInstance().setConfig(config);

            FaithTrack faithTrack = BoardParser.getInstance().getFaithTrack();
            if (faithTrack.getFinalPosition() <= 0 || faithTrack.getFinalPosition() > 30) {
                setConfig(this.config);
                return false;
            }
            int prevPoints = 0, prevPosition = 0;
            for (CheckPoint checkPoint : faithTrack.getCheckPoints()) {
                if (checkPoint.getPoints() <= prevPoints || checkPoint.getPoints() >= 100 ||
                        checkPoint.getPosition() <= prevPosition || checkPoint.getPosition() > faithTrack.getFinalPosition()) {
                    setConfig(this.config);
                    return false;
                }
                prevPoints = checkPoint.getPoints();
                prevPosition = checkPoint.getPosition();
            }

            BoardParser.getInstance().getDefaultProductionPower();
            ArrayList<Integer> depotSizes = BoardParser.getInstance().getDepotSizes();
            int totalWarehouseSize = 0;
            if (depotSizes.size() <= 0 || depotSizes.size() > 4) {
                setConfig(this.config);
                return false;
            }
            for (int depotSize : depotSizes) {
                if (depotSize <= 0 || depotSize > 5) {
                    setConfig(this.config);
                    return false;
                }
                totalWarehouseSize += depotSize;
            }
            if (BoardParser.getInstance().getDevelopmentCardsSlots() <= 0 || BoardParser.getInstance().getDevelopmentCardsSlots() > 4) {
                setConfig(this.config);
                return false;
            }

            VaticanReportSection vaticanReportSection;
            int prevVRSPoints = 0, prevPopeSpace = 0;
            while ((vaticanReportSection = VRSParser.getInstance().getNextVRS()) != null) {
                if (vaticanReportSection.getPoints() <= prevVRSPoints || vaticanReportSection.getPoints() >= 100 ||
                        vaticanReportSection.getFirstSpace() <= prevPopeSpace || vaticanReportSection.getFirstSpace() > vaticanReportSection.getPopeSpace() ||
                        vaticanReportSection.getPopeSpace() > faithTrack.getFinalPosition()) {
                    setConfig(this.config);
                    return false;
                }
                prevVRSPoints = vaticanReportSection.getPoints();
                prevPopeSpace = vaticanReportSection.getPopeSpace();
            }

            HashMap<CardColour, HashSet<CardLevel>> devCardMap = new HashMap<>();
            DevCard devCard;
            int devCardCount = 0;
            while ((devCard = DevCardsParser.getInstance().getNextCard()) != null) {
                ++devCardCount;
                if (!devCardMap.containsKey(devCard.getColour())) {
                    devCardMap.put(devCard.getColour(), new HashSet<>());
                }
                devCardMap.get(devCard.getColour()).add(devCard.getLevel());
            }
            for (CardColour cardColour : CardColour.values()) {
                if (!devCardMap.containsKey(cardColour) || devCardMap.get(cardColour).size() != CardLevel.values().length) {
                    setConfig(this.config);
                    return false;
                }
            }

            if (devCardCount > 100) {
                setConfig(this.config);
                return false;
            }

            int leaderCardsCount = 0;
            while (LeaderCardsParser.getInstance().getNextCard() != null) {
                ++leaderCardsCount;
            }

            if (leaderCardsCount > 30) {
                setConfig(this.config);
                return false;
            }

            if (InitGameParser.getInstance().getLeaderCardsToAssign() <= 0 || 4 * InitGameParser.getInstance().getLeaderCardsToAssign() > leaderCardsCount) {
                setConfig(this.config);
                return false;
            }
            if (InitGameParser.getInstance().getLeaderCardsToDiscard() < 0 || InitGameParser.getInstance().getLeaderCardsToDiscard() >= InitGameParser.getInstance().getLeaderCardsToAssign()) {
                setConfig(this.config);
                return false;
            }

            int prevInitResources = 0, prevInitFaithPoints = 0;
            for (int i = 0; i < 3; ++i) {
                int initResources = InitGameParser.getInstance().getInitResources(i);
                int initFaithPoints = InitGameParser.getInstance().getInitFaithPoints(i);

                if (initResources < prevInitResources || initResources > totalWarehouseSize ||
                        initFaithPoints < prevInitFaithPoints || initFaithPoints >= faithTrack.getFinalPosition()) {
                    setConfig(this.config);
                    return false;
                }

                prevInitResources = initResources;
                prevInitFaithPoints = initFaithPoints;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
