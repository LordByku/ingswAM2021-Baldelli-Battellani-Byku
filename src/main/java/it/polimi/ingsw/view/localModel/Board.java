package it.polimi.ingsw.view.localModel;

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

public class Board extends LocalModelElement implements CLIPrintable {
    private FaithTrack faithTrack;
    private Warehouse warehouse;
    private Strongbox strongbox;
    private DevCardArea devCards;
    private PlayedLeaderCardsArea playedLeaderCards;
    private HandLeaderCardsArea handLeaderCards;

    @Override
    public void updateModel(JsonElement boardJson) {
        JsonObject boardObject = boardJson.getAsJsonObject();
        if (boardObject.has("faithTrack")) {
            JsonElement faithTrackJson = boardObject.get("faithTrack");
            faithTrack = gson.fromJson(faithTrackJson, FaithTrack.class);
            faithTrack.updateModel(faithTrackJson);
        }
        if (boardObject.has("warehouse")) {
            JsonElement warehouseJson = boardObject.get("warehouse");
            warehouse = gson.fromJson(warehouseJson, Warehouse.class);
            warehouse.updateModel(warehouseJson);
        }
        if (boardObject.has("strongbox")) {
            JsonElement strongboxJson = boardObject.get("strongbox");
            strongbox = gson.fromJson(strongboxJson, Strongbox.class);
            strongbox.updateModel(strongboxJson);
        }
        if (boardObject.has("devCards")) {
            JsonElement devCardsJson = boardObject.get("devCards");
            devCards = gson.fromJson(devCardsJson, DevCardArea.class);
            devCards.updateModel(devCardsJson);
        }
        if (boardObject.has("playedLeaderCards")) {
            JsonElement playedLeaderCardsJson = boardObject.get("playedLeaderCards");
            playedLeaderCards = gson.fromJson(playedLeaderCardsJson, PlayedLeaderCardsArea.class);
            playedLeaderCards.updateModel(playedLeaderCardsJson);
        }
        if (boardObject.has("handLeaderCards")) {
            JsonElement handLeaderCardsJson = boardObject.get("handLeaderCards");
            handLeaderCards = gson.fromJson(handLeaderCardsJson, HandLeaderCardsArea.class);
            handLeaderCards.updateModel(handLeaderCardsJson);
        }

        notifyObservers();
    }

    public ArrayList<Integer> getHandLeaderCards() {
        return handLeaderCards.getLeaderCards();
    }

    public ArrayList<ConcreteResourceSet> getWarehouse() {
        return warehouse.getDepots();
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public ArrayList<Integer> getPlayedLeaderCards() {
        return playedLeaderCards.getLeaderCards();
    }

    public ConcreteResourceSet getStrongBox() {
        return strongbox.getContent();
    }

    public ArrayList<Integer> getDevCardDeck(int deckIndex) {
        return getDevCards().get(deckIndex);
    }

    public ArrayList<ArrayList<Integer>> getDevCards() {
        return devCards.getDecks();
    }

    public HashMap<Integer, ProductionDetails> activeProductionDetails() {
        HashMap<Integer, ProductionDetails> map = new HashMap<>();
        map.put(0, LocalConfig.getInstance().getDefaultProductionPower());
        for (int i = 0; i < getDevCards().size(); i++) {
            ArrayList<Integer> deck = getDevCards().get(i);
            if (!deck.isEmpty()) {
                int devCardID = deck.get(deck.size() - 1);
                DevCard devCard = DevCardsParser.getInstance().getCard(devCardID);
                map.put(i + 1, devCard.getProductionPower());
            }
        }
        int index = getDevCards().size() + 1;
        for (int leaderCardID : getPlayedLeaderCards()) {
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
