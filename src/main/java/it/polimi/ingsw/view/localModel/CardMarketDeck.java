package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.cli.CLIPrintable;

public class CardMarketDeck extends LocalModelElement implements CLIPrintable {
    private Integer topCard;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public Integer getTopCard() {
        return topCard;
    }

    @Override
    public void updateModel(JsonElement cardMarketDeckJson) {
        notifyObservers();
    }

    @Override
    public String getCLIString() {
        StringBuilder result = new StringBuilder();
        if (topCard == null) {
            result.append("This deck is empty");
        } else {
            DevCard devCard = DevCardsParser.getInstance().getCard(topCard);
            result.append(devCard.getCLIString());
        }

        return result.toString();
    }
}
