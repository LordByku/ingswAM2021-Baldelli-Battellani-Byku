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
import it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

import java.util.ArrayList;
import java.util.HashMap;

public class Board extends LocalModelElement implements CLIPrintable {
    private FaithTrack faithTrack;
    private Warehouse warehouse;
    private Strongbox strongbox;
    private DevCardsArea devCards;
    private PlayedLeaderCardsArea playedLeaderCards;
    private HandLeaderCardsArea handLeaderCards;

    @Override
    public void updateModel(JsonElement boardJson) {
        JsonObject boardObject = boardJson.getAsJsonObject();
        if (boardObject.has("faithTrack")) {
            JsonElement faithTrackJson = boardObject.get("faithTrack");
            faithTrack.updateModel(faithTrackJson);
        }
        if (boardObject.has("devCards")) {
            JsonElement devCardsJson = boardObject.get("devCards");
            devCards.updateModel(devCardsJson);
        }
        if (boardObject.has("playedLeaderCards")) {
            JsonElement playedLeaderCardsJson = boardObject.get("playedLeaderCards");
            playedLeaderCards.updateModel(playedLeaderCardsJson);
        }
        if (boardObject.has("handLeaderCards")) {
            JsonElement handLeaderCardsJson = boardObject.get("handLeaderCards");
            handLeaderCards.updateModel(handLeaderCardsJson);
        }
        if (boardObject.has("warehouse")) {
            JsonElement warehouseJson = boardObject.get("warehouse");
            warehouse.updateModel(warehouseJson);
        }
        if (boardObject.has("strongbox")) {
            JsonElement strongboxJson = boardObject.get("strongbox");
            strongbox.updateModel(strongboxJson);
        }

        notifyObservers(NotificationSource.BOARD);
    }

    public HandLeaderCardsArea getHandLeaderCards() {
        return handLeaderCards;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public PlayedLeaderCardsArea getPlayedLeaderCards() {
        return playedLeaderCards;
    }

    public Strongbox getStrongBox() {
        return strongbox;
    }

    public DevCardsArea getDevCardsArea() {
        return devCards;
    }

    public HashMap<Integer, ProductionDetails> activeProductionDetails() {
        ArrayList<ArrayList<Integer>> devCardsId = devCards.getDecks();

        HashMap<Integer, ProductionDetails> map = new HashMap<>();
        map.put(0, LocalConfig.getInstance().getDefaultProductionPower());
        for (int i = 0; i < devCardsId.size(); i++) {
            ArrayList<Integer> deck = devCardsId.get(i);
            if (!deck.isEmpty()) {
                int devCardID = deck.get(deck.size() - 1);
                DevCard devCard = DevCardsParser.getInstance().getCard(devCardID);
                map.put(i + 1, devCard.getProductionPower());
            }
        }
        int index = devCardsId.size() + 1;
        for (int leaderCardID : getPlayedLeaderCards().getLeaderCards()) {
            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(leaderCardID);
            if (leaderCard.isType(LeaderCardType.PRODUCTION)) {
                ProductionLeaderCard productionLeaderCard = (ProductionLeaderCard) leaderCard;
                map.put(index++, productionLeaderCard.getProductionPower());
            }
        }

        return map;
    }

    public ConcreteResourceSet getResources() {
        ConcreteResourceSet result = new ConcreteResourceSet();

        for (ConcreteResourceSet depot : warehouse.getDepots()) {
            result.union(depot);
        }
        result.union(strongbox.getContent());

        return result;
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
