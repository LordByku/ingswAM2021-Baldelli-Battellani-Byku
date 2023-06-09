package it.polimi.ingsw.view.localModel;

import com.google.gson.JsonElement;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

import java.util.ArrayList;

public class DevCardsArea extends LocalModelElement {
    private ArrayList<ArrayList<Integer>> decks;

    @Override
    public void updateModel(JsonElement devCardsJson) {
        decks.clear();
        for (JsonElement deckJson : devCardsJson.getAsJsonObject().getAsJsonArray("decks")) {
            ArrayList<Integer> deck = new ArrayList<>();
            for (JsonElement cardJson : deckJson.getAsJsonArray()) {
                deck.add(cardJson.getAsInt());
            }
            decks.add(deck);
        }
        notifyObservers(NotificationSource.DEVCARDSAREA);
    }

    public ArrayList<ArrayList<Integer>> getDecks() {
        return decks;
    }
}
