package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.leaderCards.ProductionLeaderCard;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.cli.CLIPrintable;

import java.util.ArrayList;
import java.util.HashMap;

public class Board implements LocalModelElement, CLIPrintable {
    private FaithTrack faithTrack;
    private ArrayList<ConcreteResourceSet> warehouse;
    private ConcreteResourceSet strongbox;

    private ArrayList<ArrayList<Integer>> devCards;
    private ArrayList<Integer> playedLeaderCards;
    private ArrayList<Integer> handLeaderCards;

    @Override
    public void updateModel(JsonObject boardJson) {
        if (boardJson.has("faithTrack")) {
            faithTrack = gson.fromJson(boardJson.getAsJsonObject("faithTrack"), FaithTrack.class);
        }
        if (boardJson.has("warehouse")) {
            warehouse = new ArrayList<>();
            JsonArray depotArray = boardJson.getAsJsonArray("warehouse");
            for (JsonElement depotJsonElement : depotArray) {
                JsonObject depotJson = (JsonObject) depotJsonElement;
                warehouse.add(gson.fromJson(depotJson, ConcreteResourceSet.class));
            }
        }
        if (boardJson.has("strongbox")) {
            strongbox = gson.fromJson(boardJson.getAsJsonObject("strongbox"), ConcreteResourceSet.class);
        }
        if (boardJson.has("devCards")) {
            devCards = new ArrayList<>();
            JsonArray decks = boardJson.getAsJsonArray("devCards");
            for (JsonElement deckJsonElement : decks) {
                ArrayList<Integer> deck = new ArrayList<>();
                JsonArray cards = deckJsonElement.getAsJsonArray();
                for (JsonElement cardJsonElement : cards) {
                    deck.add(cardJsonElement.getAsInt());
                }
                devCards.add(deck);
            }
        }
        if (boardJson.has("playedLeaderCards")) {
            playedLeaderCards = new ArrayList<>();
            JsonArray cards = boardJson.getAsJsonArray("playedLeaderCards");
            for (JsonElement cardJson : cards) {
                playedLeaderCards.add(cardJson.getAsInt());
            }
        }
        if (boardJson.has("handLeaderCards")) {
            handLeaderCards = new ArrayList<>();
            JsonArray cards = boardJson.getAsJsonArray("handLeaderCards");
            for (JsonElement cardJson : cards) {
                handLeaderCards.add(gson.fromJson(cardJson, Integer.class));
            }
        }
    }

    public ArrayList<Integer> getHandLeaderCards() {
        return handLeaderCards;
    }

    public ArrayList<ConcreteResourceSet> getWarehouse() {
        return warehouse;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public ArrayList<Integer> getPlayedLeaderCards() {
        return playedLeaderCards;
    }

    public ConcreteResourceSet getStrongBox() {
        return strongbox;
    }

    public ArrayList<Integer> getDevCardDeck(int deckIndex) {
        return devCards.get(deckIndex);
    }

    public ArrayList<ArrayList<Integer>> getDevCards() {
        return devCards;
    }

    public HashMap<Integer, ProductionDetails> activeProductionDetails() {
        HashMap<Integer, ProductionDetails> map = new HashMap<>();
        map.put(0, LocalConfig.getInstance().getDefaultProductionPower());
        for (int i = 0; i < devCards.size(); i++) {
            ArrayList<Integer> deck = devCards.get(i);
            if (!deck.isEmpty()) {
                int devCardID = deck.get(deck.size() - 1);
                DevCard devCard = DevCardsParser.getInstance().getCard(devCardID);
                map.put(i + 1, devCard.getProductionPower());
            }
        }
        int index = devCards.size() + 1;
        for (int leaderCardID : playedLeaderCards) {
            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(leaderCardID);
            if (leaderCard.isType(LeaderCardType.PRODUCTION)) {
                ProductionLeaderCard productionLeaderCard = (ProductionLeaderCard) leaderCard;
                map.put(index++, productionLeaderCard.getProductionPower());
            }
        }

        return map;
    }

    @Override
    public String getCLIString() {
        return "[0] Check Faith Track\n" +
                "[1] Check Warehouse\n" +
                "[2] Check Strongbox\n" +
                "[3] Check Development Cards\n" +
                "[4] Check Played Leader Cards\n" +
                "[5] Check Hand Leader Cards\n" +
                "[x] Back";
    }
}
