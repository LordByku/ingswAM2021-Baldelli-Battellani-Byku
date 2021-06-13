package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.gameZone.marbles.MarbleColour;

public class MarbleMarket extends LocalModelElement {
    private MarbleColour[][] market;
    private MarbleColour freeMarble;

    public int getRows() {
        return market.length;
    }

    public int getColumns() {
        if (getRows() == 0) {
            return 0;
        }
        return market[0].length;
    }

    public String getCLIString() {
        StringBuilder result = new StringBuilder(" ");
        for (int i = 0; i < getColumns(); ++i) {
            result.append("   ");
        }
        result.append(freeMarble.getCLIString()).append("\n");
        for (int i = 0; i < market.length; i++) {
            MarbleColour[] marbleColours = market[i];
            result.append(" ");
            for (MarbleColour marbleColour : marbleColours) {
                result.append(" ").append(marbleColour.getCLIString()).append(" ");
            }
            result.append("< [").append(i).append("]\n");
        }
        result.append(" ");
        for (int i = 0; i < getColumns(); ++i) {
            result.append(" ^ ");
        }
        result.append(" \n ");
        for (int i = 0; i < getColumns(); ++i) {
            result.append("[").append(i).append("]");
        }
        result.append(" \n");
        return result.toString();
    }

    @Override
    public void updateModel(JsonElement marketJson) {
        JsonObject marketObject = marketJson.getAsJsonObject();
        market = gson.fromJson(marketObject.get("market"), MarbleColour[][].class);
        freeMarble = gson.fromJson(marketObject.get("freeMarble"), MarbleColour.class);
        notifyObservers();
    }
}
