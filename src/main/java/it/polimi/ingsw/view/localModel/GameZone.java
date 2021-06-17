package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.actionTokens.ActionToken;

public class GameZone extends LocalModelElement {
    private MarbleMarket marbleMarket;
    private CardMarket cardMarket;
    private ActionTokenDeck actionTokenDeck;

    @Override
    public void updateModel(JsonElement gameZoneJson) {
        JsonObject gameZoneObject = gameZoneJson.getAsJsonObject();
        if (gameZoneObject.has("marbleMarket")) {
            JsonElement marbleMarketJson = gameZoneObject.get("marbleMarket");
            marbleMarket.updateModel(marbleMarketJson);
        }
        if (gameZoneObject.has("cardMarket")) {
            JsonElement cardMarketJson = gameZoneObject.get("cardMarket");
            cardMarket.updateModel(cardMarketJson);
        }
        if (gameZoneObject.has("actionTokenDeck")) {
            JsonElement actionTokenDeckJson = gameZoneObject.get("actionTokenDeck");
            if(actionTokenDeck == null) {
                actionTokenDeck = new ActionTokenDeck();
            }
            actionTokenDeck.updateModel(actionTokenDeckJson);
        }

        notifyObservers();
    }

    public MarbleMarket getMarbleMarket() {
        return marbleMarket;
    }

    public CardMarket getCardMarket() {
        return cardMarket;
    }

    public ActionToken getActionToken() {
        if (actionTokenDeck == null) {
            return null;
        }
        return actionTokenDeck.getFlippedActionToken();
    }

    public void resetActionToken() {
        actionTokenDeck.resetActionToken();
    }
}
