package it.polimi.ingsw.network.client.localModel;

import com.google.gson.JsonObject;

public class GameZone implements LocalModelElement {
    private MarbleMarket marbleMarket;
    private CardMarket cardMarket;

    @Override
    public void updateModel(JsonObject gameZoneJson) {
        if(gameZoneJson.has("marbleMarket")) {
            marbleMarket = gson.fromJson(gameZoneJson.getAsJsonObject("marbleMarket"), MarbleMarket.class);
        }
        if(gameZoneJson.has("cardMarket")) {
            cardMarket = gson.fromJson(gameZoneJson.getAsJsonObject("cardMarket"), CardMarket.class);
        }
    }
}
