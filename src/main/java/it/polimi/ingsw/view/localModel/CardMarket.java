package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.view.cli.CLIPrintable;
import it.polimi.ingsw.view.cli.Strings;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

import java.util.ArrayList;

public class CardMarket extends LocalModelElement implements CLIPrintable {
    ArrayList<ArrayList<CardMarketDeck>> market;

    @Override
    public String getCLIString() {
        int levelWidth = 0, colourWidth = 0;
        for (CardLevel cardLevel : CardLevel.values()) {
            levelWidth = Math.max(levelWidth, Strings.getGraphemesCount(cardLevel.toString()));
        }
        for (CardColour cardColour : CardColour.values()) {
            colourWidth = Math.max(colourWidth, Strings.getGraphemesCount(cardColour.getCLIString()));
        }

        StringBuilder result = new StringBuilder();

        for (int i = CardLevel.values().length - 1; i >= 0; --i) {
            String levelString = CardLevel.values()[i].toString();
            int levelStringLength = Strings.getGraphemesCount(levelString);

            result.append("[").append(i).append("] ").append(levelString);
            for (int j = levelStringLength; j < levelWidth; ++j) {
                result.append(" ");
            }

            for (int j = 0; j < CardColour.values().length; ++j) {
                result.append(" ");
                for (int k = 0; k < colourWidth; ++k) {
                    if (k < market.get(i).get(j).getQuantity()) {
                        result.append("[");
                    } else {
                        if(k > 0 && k == market.get(i).get(j).getQuantity()) {
                            result.append("]");
                        } else {
                            result.append(" ");
                        }
                    }
                }
            }

            result.append("\n");
        }

        for (int i = 0; i < levelWidth + 4; ++i) {
            result.append(" ");
        }

        for (CardColour cardColour : CardColour.values()) {
            result.append(" ").append(cardColour.getCLIString());
        }

        result.append("\n");

        for (int i = 0; i < levelWidth + 4; ++i) {
            result.append(" ");
        }

        for (int i = 0; i < CardColour.values().length; ++i) {
            String index = "[" + i + "]";
            int indexLength = Strings.getGraphemesCount(index);
            StringBuilder centeredIndex = new StringBuilder();
            for (int j = 0; j < (colourWidth - indexLength) / 2; ++j) {
                centeredIndex.append(" ");
            }
            centeredIndex.append(index);
            for (int j = 0; j < (colourWidth - indexLength + 1) / 2; ++j) {
                centeredIndex.append(" ");
            }
            result.append(" ").append(centeredIndex);
        }

        result.append("\nInsert row and column of the deck you want to check or press [x] to go back:");
        return result.toString();
    }

    public Integer getDevCard(int row, int column) {
        return market.get(row).get(column).getTopCard();
    }

    @Override
    public void updateModel(JsonElement cardMarketJson) {
        JsonArray cardMarketArray = cardMarketJson.getAsJsonObject().getAsJsonArray("market");
        for (int i = 0; i < market.size(); ++i) {
            JsonArray marketRow = cardMarketArray.get(i).getAsJsonArray();
            for (int j = 0; j < market.get(0).size(); ++j) {
                market.get(i).get(j).updateModel(marketRow.get(j));
            }
        }
        notifyObservers(NotificationSource.CARDMARKET);
    }
}