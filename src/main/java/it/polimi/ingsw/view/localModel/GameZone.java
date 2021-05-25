package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.actionTokens.ActionToken;

public class GameZone implements LocalModelElement {
    private MarbleMarket marbleMarket;
    private CardMarket cardMarket;
    private ActionToken actionToken;

    @Override
    public void updateModel(JsonObject gameZoneJson) {
        if (gameZoneJson.has("marbleMarket")) {
            marbleMarket = gson.fromJson(gameZoneJson.getAsJsonObject("marbleMarket"), MarbleMarket.class);
        }
        if (gameZoneJson.has("cardMarket")) {
            cardMarket = gson.fromJson(gameZoneJson.getAsJsonObject("cardMarket"), CardMarket.class);
        }
        if (gameZoneJson.has("actionToken")) {
            actionToken = gson.fromJson(gameZoneJson.get("actionToken"), ActionToken.class);
        }
    }

    public MarbleMarket getMarbleMarket() {
        return marbleMarket;
    }

    public CardMarket getCardMarket() {
        return cardMarket;
    }

    public ActionToken getActionToken() {
        return actionToken;
    }

    public void resetActionToken() {
        actionToken = null;
    }
}
