package it.polimi.ingsw.network.client.localModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

import java.util.ArrayList;

public class Board implements LocalModelElement {
    private FaithTrack faithTrack;
    private ArrayList<ConcreteResourceSet> warehouse;
    private ConcreteResourceSet strongbox;
    private ArrayList<ArrayList<Integer>> devCards;
    private ArrayList<Integer> playedLeaderCards;
    private ArrayList<Integer> handLeaderCards;

    public ArrayList<Integer> getHandLeaderCards() {
        return handLeaderCards;
    }

    public ArrayList<ConcreteResourceSet> getWarehouse() {
        return warehouse;
    }

    @Override
    public void updateModel(JsonObject boardJson) {
        if(boardJson.has("faithTrack")) {
            faithTrack = gson.fromJson(boardJson.getAsJsonObject("faithTrack"), FaithTrack.class);
        }
        if(boardJson.has("warehouse")) {
            warehouse = new ArrayList<>();
            JsonArray depotArray = boardJson.getAsJsonArray("warehouse");
            for(JsonElement depotJsonElement: depotArray) {
                JsonObject depotJson = (JsonObject) depotJsonElement;
                warehouse.add(gson.fromJson(depotJson, ConcreteResourceSet.class));
            }
        }
        if(boardJson.has("strongbox")) {
            strongbox = gson.fromJson(boardJson.getAsJsonObject("strongbox"), ConcreteResourceSet.class);
        }
        if(boardJson.has("devCards")) {
            devCards = new ArrayList<>();
            JsonArray decks = boardJson.getAsJsonArray("devCards");
            for(JsonElement deckJsonElement: decks) {
                ArrayList<Integer> deck = new ArrayList<>();
                JsonArray deckJson = (JsonArray) deckJsonElement;
                for(JsonElement cardJson: deckJson) {
                    deck.add(cardJson.getAsInt());
                }
                devCards.add(deck);
            }
        }
        if(boardJson.has("playedLeaderCards")) {
            playedLeaderCards = new ArrayList<>();
            JsonArray cards = boardJson.getAsJsonArray("playedLeaderCards");
            for(JsonElement cardJson: cards) {
                playedLeaderCards.add(cardJson.getAsInt());
            }
        }
        if(boardJson.has("handLeaderCards")) {
            handLeaderCards = new ArrayList<>();
            JsonArray cards = boardJson.getAsJsonArray("handLeaderCards");
            for(JsonElement cardJson: cards) {
                handLeaderCards.add(gson.fromJson(cardJson, Integer.class));
            }
        }
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
}
